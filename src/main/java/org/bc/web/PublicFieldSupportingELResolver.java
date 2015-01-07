package org.bc.web;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

public class PublicFieldSupportingELResolver extends ELResolver{
	@Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
    	if(base==null){
    		return null;
    	}
    	if(base instanceof Map){
    		return null;
    	}
    	 if(property instanceof String && base != null) {
             try {
                 Field field = base.getClass().getDeclaredField((String) property);
                 Object value = field.get(base);
                 context.setPropertyResolved(true);
                 return value;
             }catch(NoSuchFieldException ex){
            	 return null;
             }catch (Exception e) {
                 throw new PropertyNotFoundException(e);
             }
         }
    	 return null;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return false;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }
}
