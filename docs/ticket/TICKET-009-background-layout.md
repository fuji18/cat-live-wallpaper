# TICKET-009: 背景レイアウト計算の実装

- 優先度: `P0`
- ステータス: `done`
- 依存チケット: `TICKET-004`

## 目的

背景切り出し位置とレイアウトの計算を独立コンポーネントとして実装する。

## スコープ

- 背景の表示範囲計算を実装する
- `wallpaperOffsetX` に応じた水平移動計算を実装する

## 完了条件

- [x] オフセットに応じて背景切り出し位置を計算できる（resolveSourceRect が offsetX を画像座標に変換）
- [x] サイズ変更時に再計算できる（updateViewport / updateBackground で随時更新可能）

## 参照ドキュメント

- [functional-design.md](../functional-design.md)
- [architecture.md](../architecture.md)