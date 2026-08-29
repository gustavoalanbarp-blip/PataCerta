package com.patacerta.app.ui.petprofile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.patacerta.app.data.local.entity.WeightEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Gráfico de evolução de peso desenhado diretamente em Canvas, evitando
 * dependência de bibliotecas externas de gráficos apenas para esta feature
 * simples (RF06). Recebe a lista de WeightEntry vinda do Room.
 */
public class WeightChartView extends View {

    private final List<WeightEntry> entries = new ArrayList<>();
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WeightChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint.setColor(Color.parseColor("#2A6F7F"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(6f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);

        dotPaint.setColor(Color.parseColor("#2A6F7F"));
        dotPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.parseColor("#1F2A30"));
        textPaint.setTextSize(28f);
        textPaint.setFakeBoldText(true);
    }

    public void setEntries(List<WeightEntry> newEntries) {
        entries.clear();
        if (newEntries != null) entries.addAll(newEntries);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (entries.size() < 2) {
            if (entries.size() == 1) {
                String label = formatWeight(entries.get(0).getWeightKg()) + " kg";
                canvas.drawText(label, getPaddingLeft(), getHeight() / 2f, textPaint);
            }
            return;
        }

        float paddingH = 24f;
        float paddingV = 36f;
        float width = getWidth() - 2 * paddingH;
        float height = getHeight() - 2 * paddingV;

        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (WeightEntry e : entries) {
            min = Math.min(min, e.getWeightKg());
            max = Math.max(max, e.getWeightKg());
        }
        if (max == min) { max += 1; min -= 1; }

        Path path = new Path();
        float stepX = width / (entries.size() - 1);

        for (int i = 0; i < entries.size(); i++) {
            float x = paddingH + i * stepX;
            float normalized = (float) ((entries.get(i).getWeightKg() - min) / (max - min));
            float y = paddingV + height - (normalized * height);

            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        canvas.drawPath(path, linePaint);

        // Ponto e rótulo no último registro (peso mais recente)
        WeightEntry last = entries.get(entries.size() - 1);
        float lastX = paddingH + (entries.size() - 1) * stepX;
        float normalizedLast = (float) ((last.getWeightKg() - min) / (max - min));
        float lastY = paddingV + height - (normalizedLast * height);

        canvas.drawCircle(lastX, lastY, 8f, dotPaint);
        String label = formatWeight(last.getWeightKg()) + " kg";
        float textWidth = textPaint.measureText(label);
        canvas.drawText(label, Math.min(lastX, getWidth() - textWidth - paddingH), lastY - 16f, textPaint);
    }

    private String formatWeight(double w) {
        return String.format(java.util.Locale.getDefault(), "%.1f", w).replace(".", ",");
    }
}
