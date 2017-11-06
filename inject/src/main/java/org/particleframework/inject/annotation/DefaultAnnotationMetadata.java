/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.inject.annotation;

import org.particleframework.core.convert.value.ConvertibleValues;
import org.particleframework.core.convert.value.ConvertibleValuesMap;
import org.particleframework.core.util.CollectionUtils;
import org.particleframework.core.util.StringUtils;

import java.util.*;

/**
 * Default implementation of {@link AnnotationMetadata}
 *
 * @author Graeme Rocher
 * @since 1.0
 */
class DefaultAnnotationMetadata implements AnnotationMetadata {

    private Set<String> declaredAnnotations = null;
    private Map<String, Map<CharSequence, Object>> allStereotypes = null;
    private Map<String, Map<CharSequence, Object>> allAnnotations = null;

    @Override
    public boolean hasDeclaredAnnotation(String annotation) {
        return declaredAnnotations != null && StringUtils.isNotEmpty(annotation) && declaredAnnotations.contains(annotation);
    }

    @Override
    public boolean hasAnnotation(String annotation) {
        return allAnnotations != null && StringUtils.isNotEmpty(annotation) && allAnnotations.keySet().contains(annotation);
    }

    @Override
    public boolean hasStereotype(String annotation) {
        return allStereotypes != null && StringUtils.isNotEmpty(annotation) && allStereotypes.keySet().contains(annotation);
    }

    @Override
    public ConvertibleValues<Object> getValues(String annotation) {
        if(allAnnotations != null && StringUtils.isNotEmpty(annotation)) {
            Map<CharSequence, Object> values = allAnnotations.get(annotation);
            if(values != null) {
                return ConvertibleValues.of(values);
            }
            else {
                return ConvertibleValues.of( allStereotypes.get(annotation) );
            }
        }
        return ConvertibleValuesMap.empty();
    }

    /**
     * Adds an annotation and its member values, if the annotation already exists the data will be merged with existing values replaced
     *
     * @param annotation The annotation
     * @param values The values
     * @return This {@link DefaultAnnotationMetadata}
     */
    DefaultAnnotationMetadata addAnnotation(String annotation, Map<CharSequence, Object> values) {
        if(annotation != null) {
            Map<String, Map<CharSequence, Object>> allAnnotations = getAllAnnotations();
            addAnnotation(annotation, values, Collections.emptySet(), allAnnotations, false);
        }
        return this;
    }

    /**
     * Adds a stereotype and its member values, if the annotation already exists the data will be merged with existing values replaced
     *
     * @param annotation The annotation
     * @param values The values
     * @return This {@link DefaultAnnotationMetadata}
     */
    DefaultAnnotationMetadata addStereotype(String annotation, Map<CharSequence, Object> values) {
        if(annotation != null) {
            Map<String, Map<CharSequence, Object>> allStereotypes = getAllStereotypes();
            addAnnotation(annotation, values, Collections.emptySet(), allStereotypes, false);
        }
        return this;
    }

    /**
     * Adds an annotation directly declared on the element and its member values, if the annotation already exists the data will be merged with existing values replaced
     *
     * @param annotation The annotation
     * @param values The values
     * @return This {@link DefaultAnnotationMetadata}
     */
    DefaultAnnotationMetadata addDeclaredAnnotation(String annotation, Map<CharSequence, Object> values) {
        if(annotation != null) {
            Set<String> declaredAnnotations = getDeclaredAnnotations();
            Map<String, Map<CharSequence, Object>> allAnnotations = getAllAnnotations();
            addAnnotation(annotation, values, declaredAnnotations, allAnnotations, true);
        }
        return this;
    }

    private void addAnnotation(String annotation,
                               Map<CharSequence, Object> values,
                               Set<String> declaredAnnotations, Map<String,
                               Map<CharSequence, Object>> allAnnotations,
                               boolean isDeclared) {
        if(isDeclared && declaredAnnotations != null) {
            declaredAnnotations.add(annotation);
        }
        Map<CharSequence, Object> existing = allAnnotations.get(annotation);
        boolean hasValues = CollectionUtils.isNotEmpty(values);
        if(existing != null && hasValues) {
            if(existing.isEmpty()) {
                existing = new LinkedHashMap<>();
                allAnnotations.put(annotation, existing);
            }
            existing.putAll(values);
        }
        else {
            if(!hasValues) {
                existing = Collections.emptyMap();
            }
            else {
                existing = new LinkedHashMap<>(values.size());
                existing.putAll(values);
            }
            allAnnotations.put(annotation, existing);
        }
    }

    private Map<String, Map<CharSequence, Object>> getAllStereotypes() {
        Map<String, Map<CharSequence, Object>>  stereotypes = this.allStereotypes;
        if (stereotypes == null) {
            this.allStereotypes = stereotypes = new HashMap<>(3);
        }
        return stereotypes;
    }

    private Map<String, Map<CharSequence, Object>> getAllAnnotations() {
        Map<String, Map<CharSequence, Object>>  annotations = this.allAnnotations;
        if (annotations == null) {
            this.allAnnotations = annotations = new HashMap<>(3);
        }
        return annotations;
    }

    private Set<String> getDeclaredAnnotations() {
        Set<String> annotations = this.declaredAnnotations;
        if (annotations == null) {
            this.declaredAnnotations = annotations = new HashSet<>(3);
        }
        return annotations;
    }
}
