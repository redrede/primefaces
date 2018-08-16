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
package org.primefaces.component.slider;

import javax.faces.component.UIInput;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.util.Constants;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import javax.faces.event.PhaseId;
import javax.faces.event.BehaviorEvent;
import org.primefaces.expression.SearchExpressionFacade;
import javax.faces.application.FacesMessage;
import org.primefaces.util.MessageFactory;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="components.css"),
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/jquery-plugins.js"),
	@ResourceDependency(library="primefaces", name="core.js"),
	@ResourceDependency(library="primefaces", name="components.js")
})
public class Slider extends SliderBase implements org.primefaces.component.api.Widget,javax.faces.component.behavior.ClientBehaviorHolder,org.primefaces.component.api.PrimeClientBehaviorHolder {




    private final static String DEFAULT_EVENT = "slideEnd";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<? extends BehaviorEvent>>() {{
        put("slideEnd", SlideEndEvent.class);
    }});

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
         return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();

        if(ComponentUtils.isRequestSource(this, context)) {
            Map<String,String> params = context.getExternalContext().getRequestParameterMap();
            String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            String clientId = getClientId(context);

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if(eventName.equals("slideEnd")) {
                double sliderValue = Double.parseDouble(params.get(clientId + "_slideValue"));
                SlideEndEvent slideEndEvent = new SlideEndEvent(this, behaviorEvent.getBehavior(), sliderValue);
                slideEndEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(slideEndEvent);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    public final static String VALUE_OUT_OF_RANGE = "primefaces.slider.OUT_OF_RANGE";

    @Override
    public void validate(FacesContext context) {
        super.validate(context);
        
        if (!isValid()) {
            return;
        }

        String[] inputIds = this.getFor().split(",");
        if (this.isRange()) {
            UIInput inputFrom = (UIInput) SearchExpressionFacade.resolveComponent(context, this, inputIds[0].trim());
            UIInput inputTo = (UIInput) SearchExpressionFacade.resolveComponent(context, this, inputIds[1].trim());
            String valueFromStr = getSubmittedValue(inputFrom).toString();
            String valueToStr = getSubmittedValue(inputTo).toString();
            double valueFrom = Double.valueOf(valueFromStr);
            double valueTo = Double.valueOf(valueToStr);
            if (valueTo < valueFrom) {
                this.setValid(false);
                inputFrom.setValid(false);
                inputTo.setValid(false);
            }
            else {
                if (valueFrom < this.getMinValue() || valueFrom > this.getMaxValue()) {
                    this.setValid(false);
                    inputFrom.setValid(false);
                }
                if (valueTo > this.getMaxValue() || valueTo < this.getMinValue()) {
                    this.setValid(false);
                    inputTo.setValid(false);
                }
            }
        }
        else {
            UIInput input = (UIInput) SearchExpressionFacade.resolveComponent(context, this, inputIds[0].trim());
            Object submittedValue = getSubmittedValue(input);
            if (submittedValue == null) {
                return;
            }
            
            String valueStr = submittedValue.toString();
            double value = Double.valueOf(valueStr);
            if (value < this.getMinValue() || value > this.getMaxValue()) {
                this.setValid(false);
                input.setValid(false);
            }
        }

        if (!isValid()) {
            String validatorMessage = getValidatorMessage();
            FacesMessage msg = null;
            if (validatorMessage != null) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessage, validatorMessage);
            }
            else {
                msg = MessageFactory.getMessage(VALUE_OUT_OF_RANGE, FacesMessage.SEVERITY_ERROR, null);
            }
            context.addMessage(getClientId(context), msg);
        }
    }

    private Object getSubmittedValue(UIInput input) {
        return input.getSubmittedValue() == null && input.isLocalValueSet() ? input.getValue() : input.getSubmittedValue();
    }
}