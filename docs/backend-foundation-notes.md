# MUHOU Backend Foundation Notes

## Runtime baseline

- JDK: 17
- Spring Boot: 3.5.8
- Build tool: Maven Wrapper
- Persistence: MyBatis
- Database: MySQL 8

## Local JDK note

- IDEA project SDK is `ms-17`
- Current terminal default Java is still `D:\java\jdk` which is Java 8
- Recommended local JDK 17 path: `C:\Users\Kongjiameng\.jdks\ms-17.0.18`
- Quick run script: `scripts/dev-run.ps1`

## Current local database

- Host: `127.0.0.1`
- Port: `3306`
- Database: `muhou`
- Username: `root`
- Password: `1234`

## Agreed frontend-driven business flow

- Prop status: `idle`, `locked`, `renting`, `offline`
- Project status: `editing`, `ordered`
- Order status:
  - `pending_factory_confirm`
  - `wait_pickup`
  - `renting`
  - `wait_review`
  - `completed`
  - `cancelled_timeout`

## Core modules to implement next

- User and role
- Props and warehouse
- Project schemes
- Rental orders
- Payment and refund
- Admin audit, dispute and review management

## Suggested backend package layout

- `common`: shared response, exception, config
- `application`: application services and use cases
- `domain`: domain models, enums, business rules
- `infrastructure`: persistence, third-party clients, payment integration
- `web`: controller, request DTO, response VO

## Payment design principles

- Monetary values use integer fen
- One business order can have multiple payment attempts
- Final payment result is confirmed by notify or order query, not by frontend callback
- Payment and refund notifications must be stored and processed idempotently

## Current schema assets

- canonical DDL snapshot: `src/main/resources/sql/muhou-core-ddl.sql`
- prepared Flyway migration: `src/main/resources/db/migration/V1__init_core_schema.sql`
- demo seed and compatibility migrations:
  - `src/main/resources/db/migration/V2__add_project_and_order_detail.sql`
  - `src/main/resources/db/migration/V3__seed_demo_data.sql`
  - `src/main/resources/db/migration/V4__align_rental_order_item_schema.sql`

## Current runnable APIs

- `POST /api/auth/wechat-login`
- `GET /api/users/me`
- `GET /api/props`
- `GET /api/props/{propId}`
- `GET /api/projects`
- `GET /api/projects/{projectId}`
- `POST /api/projects/editing/items`
- `POST /api/projects/{projectId}/items`
- `DELETE /api/projects/{projectId}/items/{propId}`
- `PUT /api/projects/{projectId}`
- `GET /api/orders?role=demander|supplier`
- `GET /api/orders/{orderId}`
- `POST /api/orders`
- `POST /api/orders/{orderId}/pay`
- `POST /api/factory/orders/{orderId}/confirm`
- `POST /api/factory/orders/{orderId}/outbound-scan`
- `POST /api/factory/orders/{orderId}/return-scan`
- `POST /api/orders/{orderId}/reviews`

## Current mock strategy

- WeChat login defaults to mock mode via `muhou.wechat.mock-enabled=true`
- WeChat pay defaults to mock mode via `muhou.payment.mock-enabled=true`
- Main business flow is runnable without real AppID/AppSecret or merchant payment credentials
