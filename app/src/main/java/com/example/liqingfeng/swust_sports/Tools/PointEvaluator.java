package com.example.liqingfeng.swust_sports.Tools;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

public class PointEvaluator implements TypeEvaluator<PointF>{
    @Override
    public PointF evaluate(float fraction, PointF startPointF, PointF endPointF) {
        float x = startPointF.x + fraction * (endPointF.x - startPointF.x);
        float y = startPointF.y + fraction * (endPointF.y - startPointF.y);
        return new PointF(x, y);
    }
}
