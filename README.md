NestValidation
==============

SAStrutsで、配列・リストやフォームをネストしたフォームに対するバリデーションを行う。

使い方

1.以下の３クラスをプロジェクトに入れる。  
com.sample.nestvalidation.costomizer.NestValidateActionCustomizer  
com.sample.nestvalidation.annotation.Nest  
com.sample.nestvalidation.annotation.NestArray

2.costomizer.diconを書き換え、上記のNestValidateActionCustomizerをActionCustomizerに指定する。  
org.seasar.struts.customizer.ActionCustomizer → com.sample.nestvalidation.costomizer.NestValidateActionCustomizer

3.アノテーションは以下のようにつける。  
Stringの配列・Listの属性値（サンプルでいうとParentFormのarray属性とか）  
→ 普通にアノテーションつけるだけで配列の各要素にValidationが効く。（@Requiredとか）  

Formの中に別のFormを入れる場合（サンプルでいうとParentFormのchild属性）  
→ @Nestをつけて子Formの各属性に必要なアノテーションをつける。

Formの中に別のFormの配列・リストを入れる場合（サンプルでいうとParentFormのchildrenList属性とか）  
→ @NestArrayをつけて子フォームの型をアノテーションのパラメータに指定する。  
　子Formの各属性に必要なアノテーションをつける。

以上で入れ子になった属性にバリデーションをかけれ（ると思い）ます。
