package co.vamospues.vamospues.helpers;

import android.content.Context;
import android.graphics.PointF;

import java.util.Arrays;

import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.wasabeef.picasso.transformations.gpu.GPUFilterTransformation;

/**
 * Created by Manuela Duque M on 26/04/2017.
 */

public class VignetteFilterTransformation extends GPUFilterTransformation{

    private PointF mCenter;
    private float[] mVignetteColor;
    private float mVignetteStart;
    private float mVignetteEnd;

    public VignetteFilterTransformation(Context context) {
        this(context, new PointF(0.5f, 0.5f), new float[] { 0.0f, 0.0f, 0.0f }, 0.3f, 0.399999999f);
    }

    public VignetteFilterTransformation(Context context, PointF center, float[] color, float start,
                                        float end) {
        super(context, new GPUImageVignetteFilter());
        mCenter = center;
        mVignetteColor = color;
        mVignetteStart = start;
        mVignetteEnd = end;
        GPUImageVignetteFilter filter = getFilter();
        filter.setVignetteCenter(mCenter);
        filter.setVignetteColor(mVignetteColor);
        filter.setVignetteStart(mVignetteStart);
        filter.setVignetteEnd(mVignetteEnd);
    }

    @Override public String key() {
        return "VignetteFilterTransformation(center=" + mCenter.toString() +
                ",color=" + Arrays.toString(mVignetteColor) +
                ",start=" + mVignetteStart + ",end=" + mVignetteEnd + ")";
    }


}
