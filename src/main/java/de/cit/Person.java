package de.cit;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexObjectFieldReference;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaElement;
import org.hibernate.search.engine.backend.document.model.dsl.IndexSchemaObjectField;
import org.hibernate.search.engine.backend.types.dsl.IndexFieldTypeFactory;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.PropertyBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyBinding;

import java.util.Map;

@Indexed
public class Person {
    @KeywordField
    String name;

    @KeywordField
    String surname;

    @PropertyBinding(binder =  @PropertyBinderRef(type = SplitPropertiesBinding.class))
    Map<String, String> properties;

    public Person(String name, String surname, Map<String, String> properties) {
        this.name = name;
        this.surname = surname;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", surname=" + surname + "]";
    }

    public static class SplitPropertiesBinding implements PropertyBinder {
        @Override
        public void bind(PropertyBindingContext context) {
            context.dependencies().useRootOnly();

            IndexSchemaElement schemaElement = context.indexSchemaElement();

            IndexSchemaObjectField propertiesField =
                    schemaElement.objectField("properties");

            propertiesField.fieldTemplate("properties_field_template", IndexFieldTypeFactory::asString);

            context.bridge(Map.class, new SplitPropertiesBridge(propertiesField.toReference()));
        }

        private static class SplitPropertiesBridge implements PropertyBridge<Map> {
            private final IndexObjectFieldReference propertiesField;

            public SplitPropertiesBridge(IndexObjectFieldReference propertiesField) {
                this.propertiesField = propertiesField;
            }

            @Override
            public void write(DocumentElement rootElement, Map bridgedElement, PropertyBridgeWriteContext propertyBridgeWriteContext) {

                Map<String, String> properties = (Map<String, String>) bridgedElement;
                if(properties == null) {
                    return;
                }
                DocumentElement indexedPropertyObject = rootElement.addObject(propertiesField);

                /////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////// THROW EXCEPTION here to simulate error while indexing !! ///////////////
                /// /////////////////////////////////////////////////////////////////////////////////////////////////
                if(true) {
                    throw new RuntimeException("possible runtime error");
                }

                properties.forEach((key, value) -> indexedPropertyObject.addValue(key, value));
            }
        }
    }
}
