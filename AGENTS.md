# Repository Guidelines

## プロジェクト構成・モジュール
- `composeApp/src/commonMain/kotlin`: 共有UIとロジック。プラットフォーム差分は `androidMain`/`iosMain`/`jvmMain`/`jsMain`/`wasmJsMain` に配置。
- リソースは `composeApp/src/commonMain/composeResources` を基準に、必要に応じて各プラットフォーム資産フォルダへ分岐。Web静的資産は `composeApp/src/webMain/resources`。
- Android エントリポイントとリソースは `composeApp/src/androidMain/...`。Desktop(JVM)は `composeApp/src/jvmMain/kotlin`, iOS ホストアプリは `iosApp/iosApp` 配下 (SwiftUI・設定は `Configuration/Config.xcconfig`)。
- ルートの `settings.gradle.kts` と `gradle/libs.versions.toml` でモジュール参照・バージョンを管理。

## ビルド・実行・開発コマンド
- Android デバッグ APK: `./gradlew :composeApp:assembleDebug`
- Desktop 実行: `./gradlew :composeApp:run`
- Web 実行 (Wasm/推奨): `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Web 実行 (JS/旧ブラウザ互換): `./gradlew :composeApp:jsBrowserDevelopmentRun`
- 共有チェック (テスト+検証一括): `./gradlew :composeApp:check`
- iOS: `open iosApp/iosApp.xcodeproj` で Xcode を開き、適切なシミュレータ/デバイスで実行。

## コーディングスタイル・命名
- Kotlin 公式スタイル (4 スペースインデント、`import` は IDE 並び)。IDE の Reformat で整形。
- クラス/Composable は `PascalCase`、メソッド・変数は `camelCase`、定数は `UPPER_SNAKE_CASE`。パッケージは `org.example.project` を基準に機能単位で分割。
- ファイル名は主要な公開クラス/Composable に合わせる (例: `MainActivity.kt`, `App.kt`)。UI プレビュー用コードは `androidMain` 内でのみ。
- リソース ID やファイルはプレフィックスで用途を明示 (例: `ic_launcher_*`, `styles.css`)。

## テスト指針
- 共有ロジックの単体テストは `composeApp/src/commonTest/kotlin` を使用。`kotlin.test` が既定。
- プラットフォーム固有の動作は各 `*Main` に対応するテストソースを追加。Web/wasm もブラウザテストを意識。
- 実行: `./gradlew :composeApp:check` で共通テストを一括、必要に応じ `./gradlew :composeApp:allTests` で全ターゲットを網羅。
- テスト命名は `対象メソッド_条件_期待結果` 形式を推奨し、振る舞いを説明する断言を最小限で。

## コミット・PR ガイド
- 現在ローカル履歴は参照不可のため、`Conventional Commits` 形式 (`feat: ...`, `fix: ...`, `chore: ...`) を推奨。1 コミット 1 トピックで生成物は含めない。
- コミット本文には動機と影響範囲 (対象プラットフォーム: Android/iOS/Web/Desktop) を短く記載。
- PR は目的・変更点・テスト結果を箇条書きし、UI 変更はスクリーンショット/動画を添付。関連 Issue/タスクがあればリンク。
- レビュー要求前に `./gradlew :composeApp:check` を通し、差分が意図したフォルダに入っているか再確認。

## 設定・セキュリティ
- `local.properties` で SDK パス等を指定するが、秘密情報は置かない。環境依存設定は `iosApp/Configuration/Config.xcconfig` に分離し、秘匿値は別の安全な手段で注入。
- API キーや証明書はコミットせず、サンプル値のみを `*.sample` などで共有。実鍵は各開発環境で設定。
- 追加ライブラリは `gradle/libs.versions.toml` でバージョンを集中管理し、プラットフォーム互換性を確認してから適用。

## ソースコードのコメントについて
- できるだけ細かい粒度でソースコード上に日本語のコメントを追加する
- 関数へのコメントは、利用例のサンプルコードも記載すること