# TICKET-007: Bitmap 読込経路の実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-003`, `TICKET-006`

## 目的

MVP 必須素材を一度だけ読み込む `BitmapRepository` の読込経路を実装する。

## スコープ

- `BitmapRepository.loadAll()` を実装する
- 昼背景、猫、毛糸玉の読込経路を作る

## 完了条件

- [x] 必須素材を一括読込できる（BitmapRepository.loadAll() で一括デコード、AssetSet に格納）
- [x] 毎フレーム decode しない構造になっている（loadAll() は初回のみ呼び出す設計、assetSet フィールドにキャッシュ）

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [architecture.md](../architecture.md)