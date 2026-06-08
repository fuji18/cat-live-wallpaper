// ルートプロジェクトのビルド設定。
// 各モジュールから参照するプラグインのバージョンをここで一元管理する。
// モジュール本体は TICKET-002 で追加するため、現時点では apply false のみ宣言する。
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}
