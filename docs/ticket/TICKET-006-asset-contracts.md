# TICKET-006: アセット契約の定義

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-002`

## 目的

描画レイヤーが参照する `AssetSet` と素材契約を定義する。

## スコープ

- `AssetSet` を実装する
- MVP 必須素材と任意素材の境界を定義する
- night background の nullable 契約を決める

## 完了条件

- [x] `AssetSet` が描画に必要な素材を表現できる（backgroundDay, catWalkFrames, catIdleFrame, catPlayFrames, toyYarn）
- [x] night background の未配置を許容できる（`backgroundNight: Bitmap?` で nullable 契約）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)