# TICKET-003: Manifest と壁紙メタデータの追加

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-002`

## 目的

ライブ壁紙として認識されるための Manifest と `wallpaper.xml` を追加する。

## スコープ

- `AndroidManifest.xml` を作成する
- `res/xml/wallpaper.xml` を作成する
- 壁紙サービス宣言の雛形を入れる

## 完了条件

- [x] Manifest にライブ壁紙サービス定義がある
- [x] `res/xml/wallpaper.xml` が存在する

## 参照ドキュメント

- [product-requirements.md](../product-requirements.md)
- [repository-structure.md](../repository-structure.md)