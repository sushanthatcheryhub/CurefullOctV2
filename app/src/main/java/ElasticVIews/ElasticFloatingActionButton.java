
/*
 * Copyright (C) 2017 skydoves
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

package ElasticVIews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;

import curefull.healthapp.R;


public class ElasticFloatingActionButton extends FloatingActionButton {

    private FloatingActionButton view;
    private View.OnClickListener listener;

    private float scale = 0.9f;
    private int duration = 500;

    public ElasticFloatingActionButton(Context context){
        super(context);
        onCreate();
    }

    public ElasticFloatingActionButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        onCreate();
        getAttrs(attributeSet);
    }

    public ElasticFloatingActionButton(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        onCreate();
        getAttrs(attributeSet, defStyle);
    }

    private void onCreate(){
        view = this;
        view.setClickable(true);
    }

    private void getAttrs(AttributeSet attrs)
    {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ElasticFloatingActionButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle)
    {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ElasticFloatingActionButton, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray){
        scale = typedArray.getFloat(R.styleable.ElasticFloatingActionButton_fabutton_scale, scale);
        duration = typedArray.getInt(R.styleable.ElasticFloatingActionButton_fabutton_duration, duration);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(listener != null) {
                if(view.getScaleX() == 1) {
                    ViewCompat.animate(view).setDuration(duration).scaleX(scale).scaleY(scale).setInterpolator(new CycleInterpolator(0.5f))
                            .setListener(new ViewPropertyAnimatorListener() {

                                @Override
                                public void onAnimationStart(final View view) {
                                }

                                @Override
                                public void onAnimationEnd(final View v) {
                                    onClick();
                                }

                                @Override
                                public void onAnimationCancel(final View view) {
                                }
                            })
                            .withLayer()
                            .start();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private  void onClick(){
        listener.onClick(this);
    }

}
