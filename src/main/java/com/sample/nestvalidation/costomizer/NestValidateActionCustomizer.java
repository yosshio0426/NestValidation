package com.sample.nestvalidation.costomizer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.Form;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.AnnotationUtil;
import org.seasar.struts.annotation.Validator;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.customizer.ActionCustomizer;
import org.seasar.struts.validator.S2ValidatorResources;

import com.sample.nestvalidation.annotation.Nest;
import com.sample.nestvalidation.annotation.NestArray;

public class NestValidateActionCustomizer extends ActionCustomizer {

	/* (非 Javadoc)
	 * @see org.seasar.struts.customizer.ActionCustomizer#setupValidator(org.seasar.struts.config.S2ActionMapping, org.seasar.struts.validator.S2ValidatorResources)
	 */
	@Override
	protected void setupValidator(S2ActionMapping actionMapping,
			S2ValidatorResources validatorResources) {
        Map<String, Form> forms = new HashMap<String, Form>();
        for (String methodName : actionMapping.getExecuteMethodNames()) {
            if (actionMapping.getExecuteConfig(methodName).isValidator()) {
                Form form = new Form();
                form.setName(actionMapping.getName() + "_" + methodName);
                forms.put(methodName, form);
            }
        }
        for (Class<?> clazz = actionMapping.getActionFormBeanDesc()
                .getBeanClass(); clazz != null && clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            setupValidatorForClass(validatorResources, forms, clazz);
        }
        for (Iterator<Form> i = forms.values().iterator(); i.hasNext();) {
            validatorResources.addForm(i.next());
        }
	}

	/**
     * バリデータをセットアップします。
     * 
     * 
     * @param actionMapping
     *            アクションマッピング
     * @param validatorResources
     *            検証リソース
     * @param clazz
     *            検証対象フォームクラス
     */
    private void setupValidatorForClass(
	S2ValidatorResources validatorResources, Map<String, Form> forms,
	Class<?> clazz) {
		setupValidatorForClass(validatorResources, forms, clazz,
				null, null);
	}

    /**
     * バリデータをセットアップします。
     * 
     * 
     * @param actionMapping
     *            アクションマッピング
     * @param validatorResources
     *            検証リソース
     * @param clazz
     *            検証対象フォームクラス
     * @param parentProperty
     *            フォームを入れ子にする場合にプロパティ名は親フォーム名からの連鎖にする
     * @param listProperty
     *            配列またはListとなる親プロパティ名
     *            
     */
    private void setupValidatorForClass(
			S2ValidatorResources validatorResources, Map<String, Form> forms,
			Class<?> clazz, String parentProperty, String listProperty) {
		for (Field field : ClassUtil.getDeclaredFields(clazz)) {
		    for (Annotation anno : field.getDeclaredAnnotations()) {

		        String fieldName = parentProperty == null ? field.getName() : parentProperty + "." + field.getName();
		        
		        // Nestアノテーションがついていた場合は、次のフォームにセットアップを引き継ぐ
		        if (anno.annotationType().equals(Nest.class)) {
		        	setupValidatorForClass(validatorResources, forms, field.getType(), fieldName, null);
		        	continue;
		        }

			     // NestArrayアノテーションがついていた場合は、現在のプロパティ名をリストプロパティに指定して子を呼び出す
		        if (anno.annotationType().equals(NestArray.class)) {
		        	NestArray nestArray = (NestArray)anno;
		        	setupValidatorForClass(validatorResources, forms, nestArray.value(), null, fieldName);
		        	continue;
		        }
		        
		        // NestArrayのついていない配列の場合は、プリミティブな値の配列なので、リストプロパティとプロパティ名を一緒にする
		        if (field.getType().isArray() || field.getType().equals(List.class)) {
		        	listProperty = fieldName;
		        }
		        
		        processAnnotation(fieldName, listProperty, anno, validatorResources, forms);
		    }
		}
	}

    /**
     * アノテーションを処理します。
     * 
     * 
     * @param propertyName
     *            プロパティ名
     * @param listProperty
     *            配列またはListとなる親プロパティ名
     * @param annotation
     *            アノテーション
     * @param validatorResources
     *            検証リソース
     * @param forms
     *            メソッド名をキーにしたフォームのマップ
     */
    protected void processAnnotation(String propertyName,
            String listProperty, Annotation annotation,
            S2ValidatorResources validatorResources, Map<String, Form> forms) {
        Class<? extends Annotation> annotationType = annotation
                .annotationType();
        Annotation metaAnnotation = annotationType
                .getAnnotation(Validator.class);
        if (metaAnnotation == null) {
            return;
        }
        String validatorName = getValidatorName(metaAnnotation);
        Map<String, Object> props = AnnotationUtil.getProperties(annotation);
        registerValidator(propertyName, listProperty, validatorName,
                props, validatorResources, forms);
    }

    /**
     * バリデータを登録します。
     * 
     * 
     * @param propertyName
     *            プロパティ名
     * @param listProperty
     *            配列またはListとなる親プロパティ名
     * @param validatorName
     *            バリデータ名
     * @param props
     *            バリデータのプロパティ
     * @param validatorResources
     *            検証リソース
     * @param forms
     *            メソッド名をキーにしたフォームのマップ
     */
    protected void registerValidator(String propertyName, String listProperty,
            String validatorName, Map<String, Object> props,
            S2ValidatorResources validatorResources, Map<String, Form> forms) {
        org.apache.commons.validator.Field validatorField = createField(propertyName,
                validatorName, props, validatorResources);

    	validatorField.setIndexedListProperty(listProperty);
    	validatorField.setIndexedProperty(listProperty);
    	
        for (Iterator<String> i = forms.keySet().iterator(); i.hasNext();) {
            String methodName = i.next();
            if (!isTarget(methodName, (String) props.get("target"))) {
                continue;
            }
            Form form = forms.get(methodName);
            form.addField(validatorField);
        }
    }
	
}
