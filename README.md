# Wallet Management App（家計管理アプリ）

Java / Spring Boot と React を使用して個人開発している家計管理Webアプリケーションです。

家計の収入・支出を記録・管理することを目的とし、バックエンドの設計・実装を中心に、フロントエンドとのAPI連携、データベース設計、Dockerによる環境構築、AWS EC2へのデプロイまで一貫して取り組んでいます。

## 技術スタック

| 分類 | 使用技術 |
| --- | --- |
| バックエンド | Java 17 / Spring Boot 3 |
| フロントエンド | React / JavaScript |
| データベース | MySQL 8.4 |
| ORM | Spring Data JPA / Hibernate |
| 認証・認可 | Spring Security |
| DBマイグレーション | Flyway |
| Webサーバー | Nginx |
| コンテナ | Docker / Docker Compose |
| インフラ | AWS EC2（Amazon Linux 2023） |
| ビルド | Maven |
| バージョン管理 | Git / GitHub |

## 主な機能

- ユーザー認証・ログイン機能
- 収入・支出データの登録・参照・更新・削除
- データベースを用いた家計情報の永続化
- ReactからSpring BootのREST APIを呼び出す構成

※機能追加・UI改善を継続しています。

## システム構成

```text
ユーザー（ブラウザ）
        |
        | HTTP
        v
    AWS EC2
        |
        v
      Nginx
        |
        |-- React（静的ファイル配信）
        |
        |-- /api/*
              |
              v
         Spring Boot
              |
              | Spring Data JPA
              v
            MySQL
```

Docker ComposeでNginx、Spring Boot、MySQLの3サービスを管理しています。

- Nginxのみ外部公開し、Reactのビルド成果物を配信
- `/api/`へのリクエストをSpring Bootへリバースプロキシ
- Spring BootとMySQLはDocker内部ネットワークで通信
- MySQLのデータはDocker Volumeで永続化

## 設計・実装で取り組んだこと

### 1. バックエンドの設計・実装

Spring Bootを使用し、Controller、Service、Repositoryなどの責務を分離して実装しています。

また、Spring Data JPAを利用したデータアクセスや、Spring Securityによる認証・認可について学習・実装しています。

### 2. フロントエンドとAPIの連携

ReactとSpring Bootを分離し、REST APIを介してデータをやり取りする構成を採用しています。

Nginxを利用してフロントエンドの静的ファイルを配信し、APIリクエストをバックエンドへ転送しています。

### 3. データベース管理

MySQLを利用したデータの永続化に加え、Flywayによるスキーマ変更管理を導入しています。

開発環境と本番環境でのデータベース管理方法を意識して構築しています。

### 4. Dockerによる環境構築

Docker Composeを利用して、フロントエンド配信・バックエンド・データベースをコンテナ単位で管理しています。

各サービスの役割と通信経路を整理し、データベースやアプリケーションのポートを直接外部公開しない構成としています。

### 5. AWS EC2へのデプロイ

AWS EC2上にDocker Composeによる実行環境を構築し、HTTPでのアクセスまで確認しています。

EC2のセキュリティグループ、Dockerネットワーク、Nginxのリバースプロキシ設定などを通じて、Webアプリケーションの公開に必要なインフラ構成への理解を深めています。

## 現在の開発状況

- [x] Spring Bootによるバックエンド実装
- [x] ReactとのAPI連携
- [x] MySQLによるデータ永続化
- [x] FlywayによるDBマイグレーション管理
- [x] Docker Composeによる複数サービス構成
- [x] AWS EC2へのデプロイ・HTTPアクセス確認
- [ ] DNS設定・ドメインによるアクセス
- [ ] HTTPS対応
- [ ] UI・UXの改善
- [ ] README・ドキュメントの拡充

## 今後の改善予定

- DNS設定とHTTPS対応による公開環境の整備
- ReactのUI・UX改善
- テストコードの追加・整備
- ログ・監視など運用面の改善
- セキュリティ設定の見直し

## 開発目的

本アプリケーションは、バックエンドエンジニアとして必要な技術を実践的に習得することを目的に制作しています。

単に機能を実装するだけでなく、データベース設計、API設計、認証・認可、テスト、環境構築など、Webアプリケーションを構成する各技術の役割と連携を理解することを重視しています。

今後もバックエンドを軸に専門性を高めながら、インフラ・セキュリティなどの周辺領域への理解を深めていきます。
