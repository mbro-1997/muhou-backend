# MUHOU Docker Deployment

This deployment runs:

- `mysql`: MySQL 8.0 for trial deployment.
- `backend`: Spring Boot backend image `muhou-backend:1.0.1`.
- `nginx`: HTTPS reverse proxy for WeChat Mini Program request domain.

## Local Build

```powershell
cd D:\java\SourceTreeData\muhou-backend
.\mvnw.cmd -DskipTests clean package
docker build -t muhou-backend:1.0.1 .
docker save muhou-backend:1.0.1 -o muhou-backend-1.0.1.tar
```

Upload these to the server:

```text
deploy/
muhou-backend-1.0.1.tar
```

## Server Setup

```bash
mkdir -p /opt/muhou
cd /opt/muhou
docker load -i muhou-backend-1.0.1.tar
cp deploy/.env.example deploy/.env
cp deploy/nginx/conf.d/muhou.conf.example deploy/nginx/conf.d/muhou.conf
```

Edit:

- `deploy/.env`
- `deploy/nginx/conf.d/muhou.conf`
- Put SSL certificate files into `deploy/nginx/certs/`

Start:

```bash
cd /opt/muhou/deploy
docker compose up -d
docker compose logs -f backend
```

## Clean Re-deploy From Empty Database

Use this when the server database was created from an old SQL dump or an older Flyway history and the backend keeps restarting because of migration validation errors.

Back up first if the server has data you need to keep. The commands below delete the MySQL container data for this deployment.

```bash
cd /opt/muhou/deploy
docker compose down
tar -czf /opt/muhou/mysql-backup-before-reset-$(date +%Y%m%d%H%M%S).tar.gz mysql || true
rm -rf mysql/data/*
docker load -i /opt/muhou/muhou-backend-1.0.1.tar
docker compose up -d mysql backend nginx
docker compose logs -f backend
```

The backend now contains a single baseline Flyway migration. On an empty database it creates the full schema automatically, including:

```text
sys_user
sys_user_role
sys_user_wechat
factory_invite_code
factory_profile
supplier_settlement_audit
prop_info
prop_image
prop_qr_code
prop_audit
project_scheme
project_scheme_item
rental_order
rental_order_item
order_payment
payment_notify_log
order_refund
order_review
order_dispute
user_credit_log
```

Do not import the old 22-table SQL dump into the new empty database before startup. Let Flyway create the schema first.

## Security Notes

- Do not expose MySQL port `3306` to the internet.
- Only open `80`, `443`, and `22` in the cloud security group.
- The current backend still writes mini program code images to `./uploads/qrcode`; this directory is mounted into the backend container to avoid data loss during trial deployment.
- Use COS for business files in production. MySQL stores only URLs and object keys. The local upload mount should be replaced by COS integration before formal launch.
- Replace every `change-me` value before going online.
