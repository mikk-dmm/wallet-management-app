# Wallet Management App（家計管理アプリ）

Java / Spring BootとReactを使用して個人開発している家計管理Webアプリケーションです。

家計の予算・支出を記録・管理することを目的としたWebアプリケーションです。実生活での利用に加え、Webアプリケーション全体の構成や処理の流れを体系的に理解することを学習目的とし、バックエンドの設計・実装を中心に、フロントエンドとのAPI連携、データベース設計、Dockerによる環境構築、AWS EC2へのデプロイまで一貫して取り組んでいます。

## 公開環境・デモ

**公開URL：** https://walllet-mkk.duckdns.org/login

AWS EC2上にデプロイし、独自のサブドメインによる名前解決とHTTPSによるアクセスを確認しています。

### 現在の利用制限

Spring Securityによるログイン認証は実装済みですが、新規ユーザー登録画面は未実装です。

また、公開環境にはデモユーザーがまだ登録されていないため、現時点ではログイン後の家計管理機能を操作できません。

現在、ポートフォリオ評価用として、以下の環境を整備しています。

- デモユーザーの作成
- カテゴリ・支払方法・予算・支出のサンプルデータ投入
- ログイン後の主要機能の動作確認

デモ環境の整備完了後、利用方法を追記する予定です。

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
| DNS | DuckDNS |
| HTTPS / TLS | Let's Encrypt / Certbot |
| ビルド | Maven |
| バージョン管理 | Git / GitHub |

## 主な機能

- Spring Securityによるユーザー認証・ログイン
- 支出データの登録・一覧表示・編集・削除
- カテゴリ・支払方法の管理
- 月次予算・カテゴリ別予算の管理
- データベースを用いた家計情報の永続化
- ReactからSpring BootのREST APIを呼び出す構成

※機能追加・UI改善を継続しています。

## システム構成

```text
ユーザー（ブラウザ）
        |
        | DNS名前解決（DuckDNS）
        v
    Elastic IP
        |
        | HTTPS（TCP 443 / TLS）
        v
    AWS EC2
        |
        v
      Nginx
        |  TLS終端
        |
        |-- React（静的ファイル配信）
        |
        |-- /api/*
              |
              | HTTP（Docker内部ネットワーク）
              v
         Spring Boot
              |
              | Spring Data JPA / Hibernate
              v
            MySQL
```

Docker ComposeでNginx、Spring Boot、MySQLの3サービスを管理しています。

- Nginxのみ外部公開し、Reactのビルド成果物を配信
- NginxでTLSを終端し、HTTPからHTTPSへリダイレクト
- `/api/`へのリクエストをSpring Bootへリバースプロキシ
- Spring BootとMySQLはDocker内部ネットワークで通信
- MySQLのデータはDocker Volumeで永続化

## 設計・実装で取り組んだこと

### 1. バックエンドの設計・実装

Spring Bootを使用し、Controller、Service、Repositoryなどの責務を分離して実装しています。

Spring Data JPAを利用したデータアクセスや、Spring Securityによる認証・認可についても学習・実装しています。

### 2. フロントエンドとAPIの連携

ReactとSpring Bootを分離し、REST APIを介してデータをやり取りする構成を採用しています。

Nginxを利用してフロントエンドの静的ファイルを配信し、APIリクエストをバックエンドへ転送しています。

### 3. データベース設計・管理

MySQLを利用したデータの永続化に加え、Flywayによるスキーマ変更管理を導入しています。

ユーザーごとのデータ管理、テーブル間の外部キー制約、一意制約などを考慮してデータベースを設計しています。

また、支出データの検索処理について、SQLの実行計画を確認し、インデックスの設計・検証にも取り組みました。

### 4. Dockerによる環境構築

Docker Composeを利用して、フロントエンド配信・バックエンド・データベースをコンテナ単位で管理しています。

各サービスの役割と通信経路を整理し、データベースやアプリケーションのポートを直接外部公開しない構成としています。

### 5. AWS EC2へのデプロイ・HTTPS化

AWS EC2上にDocker Composeによる実行環境を構築し、HTTPSによる外部アクセスを確認しています。

DuckDNSを利用してサブドメインとEC2のElastic IPを対応付け、Let's Encryptで取得したTLS証明書をNginxに設定しています。

NginxでTLSを終端し、HTTPアクセスをHTTPSへリダイレクトする構成としました。

また、Certbotとsystemd timerを利用した証明書の自動更新処理を設定し、更新テストも実施しています。

これらの作業を通じて、DNS、HTTP / HTTPS、TLS、リバースプロキシ、Dockerネットワークなど、Webアプリケーションの公開に必要な技術への理解を深めています。

## 現在の開発状況

- [x] Spring Bootによるバックエンド実装
- [x] ReactとのREST API連携
- [x] MySQLによるデータ永続化
- [x] Spring Securityによるログイン認証
- [x] FlywayによるDBマイグレーション管理
- [x] SQL実行計画の確認・インデックス検証
- [x] Docker Composeによる複数サービス構成
- [x] AWS EC2へのデプロイ
- [x] DNS設定・サブドメインによるアクセス
- [x] Let's EncryptによるTLS証明書取得
- [x] HTTPS対応・HTTPからHTTPSへのリダイレクト
- [x] TLS証明書の自動更新設定・テスト
- [ ] デモユーザー・デモデータの整備
- [ ] 公開環境での主要機能の動作確認
- [ ] UI・UXの改善
- [ ] README・ドキュメントの拡充

## 今後の改善予定

- デモアカウントとサンプルデータによる評価環境の整備
- 公開環境での主要機能の動作確認
- ReactのUI・UX改善
- テストコードの追加・整備
- ログ・監視など運用面の改善
- セキュリティ設定の見直し
- 家庭での継続利用を通じた課題の洗い出しと、操作性・利便性を重視したUI・UXの改善

## 開発目的

本アプリケーションは、バックエンドエンジニアとして必要な技術を実践的に習得することを目的に制作しています。

単に機能を実装するだけでなく、データベース設計、API設計、認証・認可、テスト、環境構築など、Webアプリケーションを構成する各技術の役割と連携を理解することを重視しています。

今後もバックエンドを軸に専門性を高めながら、インフラ・セキュリティなどの周辺領域への理解を深めていきます。

更新日時　2026/11/19
