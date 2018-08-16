/*
 * Copyright 2009-2018 PrimeTek.
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
package org.primefaces.component.selectoneradio;

import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.component.UINamingContainer;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.faces.render.Renderer;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import java.util.List;
import java.util.ArrayList;
import org.primefaces.util.ComponentUtils;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.faces.component.UINamingContainer;


public abstract class SelectOneRadioBase extends HtmlSelectOneRadio implements org.primefaces.component.api.Widget {


	public static final String COMPONENT_TYPE = "org.primefaces.component.SelectOneRadio";
	public static final String COMPONENT_FAMILY = "org.primefaces.component";
	public static final String DEFAULT_RENDERER = "org.primefaces.component.SelectOneRadioRenderer";

	public enum PropertyKeys {

		widgetVar
		,columns
		,plain;
	}

	public SelectOneRadioBase() {
		setRendererType(DEFAULT_RENDERER);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public java.lang.String getWidgetVar() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}
	public void setWidgetVar(java.lang.String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
	}

	public int getColumns() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.columns, 0);
	}
	public void setColumns(int _columns) {
		getStateHelper().put(PropertyKeys.columns, _columns);
	}

	public boolean isPlain() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.plain, false);
	}
	public void setPlain(boolean _plain) {
		getStateHelper().put(PropertyKeys.plain, _plain);
	}

	public String resolveWidgetVar() {
		return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}
}