# TICKET-008: Bitmap 解放と縮退方針の実装

- 優先度: `P0`
- ステータス: `todo`
- 依存チケット: `TICKET-007`

## 目的

Bitmap 解放と読込失敗時の縮退方針を実装し、後続の復旧性強化の前提を作る。

## スコープ

- `BitmapRepository.clear()` を実装する
- night background 未配置時の扱いを決める
- 読込失敗時の最小構成フォールバック方針を反映する

## 完了条件

- [ ] Bitmap と参照を解放できる
- [ ] 必須以外の素材がなくても継続可能である

## 参照ドキュメント

- [architecture.md](../architecture.md)
- [development-guidelines.md](../development-guidelines.md)