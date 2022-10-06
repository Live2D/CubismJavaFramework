[English](README.md) / [日本語](README.ja.md)

---

# Cubism Java Framework

Live2D Cubism 4 Editor で出力したモデルをアプリケーションで利用するためのフレームワークです。

モデルを表示、操作するための各種機能を提供します。モデルをロードするにはCubism Coreライブラリと組み合わせて使用します。

このリポジトリは**alpha版**となっています。バグ報告やご提案がありましたら、GitHubの機能で[issue](https://github.com/Live2D/CubismJavaFramework/issues)を立てて弊社までお寄せください。

## 対応Javaバージョン

このFrameworkは**Java SE 6**以上でコンパイルが可能です。

## ライセンス

本フレームワークを使用する前に、[ライセンス](LICENSE.md)をご確認ください。

## コンポーネント

各コンポーネントはパッケージごとに提供しています。

### effect

自動まばたきやリップシンクなど、モデルに対してモーション情報をエフェクト的に付加する機能を提供します。

### exception

Cubism SDK frameworkに関連する例外クラス群を提供します。

### id

モデルに設定されたパラメータ名・パーツ名・Drawable 名を独自の型で管理する機能を提供します。

### math

行列計算やベクトル計算など、モデルの操作や描画に必要な算術演算の機能を提供します。

### model

モデルを取り扱うための各種機能（生成、更新、破棄）を提供します。

### motion

モデルにモーションデータを適用するための各種機能（モーション再生、パラメータブレンド）を提供します。

### physics

モデルに物理演算による変形操作を適用するための機能を提供します。

### rendering

各種プラットフォームでモデルを描画するためのグラフィックス命令を実装したレンダラを提供します。

### utils

JSON パーサーやログ出力などのユーティリティ機能を提供します。

## Live2D Cubism Core for Java

当リポジトリには Live2D Cubism Core for Java は同梱されていません。

ダウンロードするには[こちら](https://creatorsforum.live2d.com/t/topic/1110)のページを参照ください。

## サンプル

標準的なアプリケーションの実装例については、下記サンプルリポジトリを参照ください。

[CubismJavaSamples](https://github.com/Live2D/CubismJavaSamples)

## マニュアル

[Cubism SDK Manual](https://docs.live2d.com/cubism-sdk-manual/top/)

## 変更履歴

当リポジトリの変更履歴については [CHANGELOG.md](CHANGELOG.md) を参照ください。

## コミュニティ

ユーザー同士でCubism SDKの活用方法の提案や質問をしたい場合は、是非コミュニティをご活用ください。

- [Live2D 公式コミュニティ](https://creatorsforum.live2d.com/)
- [Live2D community(English)](https://community.live2d.com/)
