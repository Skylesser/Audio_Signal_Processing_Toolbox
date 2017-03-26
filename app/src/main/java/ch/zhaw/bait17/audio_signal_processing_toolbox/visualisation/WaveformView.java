/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.zhaw.bait17.audio_signal_processing_toolbox.visualisation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import ch.zhaw.bait17.audio_signal_processing_toolbox.R;

/**
 *
 * @author georgrem, stockan1
 */
public class WaveformView extends AudioView {

    private Paint strokePaint, fillPaint, markerPaint;
    private int width, height;
    private float centerY;
    private short[] samples;
    private float[] waveformPoints;

    public WaveformView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WaveformView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        setWillNotDraw(false);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WaveformView, defStyle, 0);

        float strokeThickness = a.getFloat(R.styleable.WaveformView_waveformStrokeThickness, 2f);
        int strokeColor = a.getColor(R.styleable.WaveformView_waveformColor,
                ContextCompat.getColor(context, R.color.default_waveform));
        int mFillColor = a.getColor(R.styleable.WaveformView_waveformFillColor,
                ContextCompat.getColor(context, R.color.default_waveformFill));
        int mMarkerColor = a.getColor(R.styleable.WaveformView_playbackIndicatorColor,
                ContextCompat.getColor(context, R.color.default_playback_indicator));

        a.recycle();

        strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeThickness);
        strokePaint.setAntiAlias(false);

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
        fillPaint.setColor(mFillColor);

        markerPaint = new Paint();
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(0);
        markerPaint.setAntiAlias(true);
        markerPaint.setColor(mMarkerColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        centerY = height / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (waveformPoints != null) {
            canvas.drawLines(waveformPoints, strokePaint);
        }
    }

    public void setSamples(short[] samples) {
        this.samples = samples;
        if (this.samples != null) {
            onSamplesChanged();
        }
    }

    private void onSamplesChanged() {
        waveformPoints = new float[width * 4];
        drawWaveform(samples);
        postInvalidate();
    }

    private void drawWaveform(short[] samples) {
        float lastX = -1;
        float lastY = -1;
        int pointIndex = 0;
        float max = Short.MAX_VALUE;

        /* For efficiency, we don't draw all of the samples in the buffer, but only the ones
           that align with pixel boundaries. */
        for (int x = 0; x < width; x++) {
            int index = (int) (((x * 1.0f) / width) * samples.length);
            short sample = samples[index];
            float y = centerY - ((sample / max) * centerY);

            if (lastX != -1) {
                waveformPoints[pointIndex++] = lastX;
                waveformPoints[pointIndex++] = lastY;
                waveformPoints[pointIndex++] = x;
                waveformPoints[pointIndex++] = y;
            }

            lastX = x;
            lastY = y;
        }
    }

    private float getFontSize(Context ctx, int textAppearance) {
        TypedValue typedValue = new TypedValue();
        ctx.getTheme().resolveAttribute(textAppearance, typedValue, true);
        int[] textSizeAttr = new int[] { android.R.attr.textSize };
        TypedArray arr = ctx.obtainStyledAttributes(typedValue.data, textSizeAttr);
        float fontSize = arr.getDimensionPixelSize(0, -1);
        arr.recycle();
        return fontSize;
    }

}
