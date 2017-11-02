package ru.fintech.db.hw1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Здесь короче вьюха с графиком. Воооот
 */
public class GraphView extends View {

    private Paint paint;
    private float[] values;
    private Graph graph;
    private Path p;
    private int lx0, lx1, ly0, ly1, h1;


    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setGraph(Graph g){
        graph = g;
        values = new float[(g.getLength() -1)*4];
    }

    private void init() {
        p = new Path();
        paint = new Paint();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int titlesz = getResources().getDimensionPixelSize(R.dimen.font_sz) * 3 / 2;
        final int titleaxissz = getResources().getDimensionPixelSize(R.dimen.font_sz);
        final int ticksz = titleaxissz / 2;
        final int tickfreq = getResources().getDimensionPixelSize(R.dimen.ticks);
        final int innerPadding = getResources().getDimensionPixelSize(R.dimen.inner_padding);
        final int outerPadding = getResources().getDimensionPixelSize(R.dimen.outer_padding);
        final int textPadding =  getResources().getDimensionPixelSize(R.dimen.text_padding);
        final int tickLength = getResources().getDimensionPixelSize(R.dimen.tick_length);

        int graphWidth = getWidth()-2*outerPadding;
        int graphHeight = getHeight()-2*outerPadding;
        int graphX = outerPadding;
        int graphY = outerPadding;

        paint.setColor(Color.BLACK);
        paint.setTextAlign(Align.CENTER);

        paint.setTextSize(titlesz);
        if (graph.getTitle() != null) {
            canvas.drawText(graph.getTitle(), graphX+graphWidth/2, graphY+titlesz, paint);
            graphY += titlesz+outerPadding;
            graphHeight -= titlesz+outerPadding;
        }

        paint.setTextSize(titleaxissz);
        if (graph.getHorizontalLabel() != null) {
            canvas.drawText(graph.getHorizontalLabel(), graphX+graphWidth/2, graphY+graphHeight, paint);
            graphHeight -= titleaxissz + textPadding;
        }

        if (graph.getVerticalLabel() != null){
            p.reset();
            p.setLastPoint(graphX+titleaxissz, graphY + graphHeight);
            p.lineTo(graphX+titleaxissz, graphY);
            canvas.drawTextOnPath(graph.getVerticalLabel(), p, 5, 0, paint);
            graphX += titleaxissz + textPadding;
            graphWidth -= titleaxissz + textPadding;
        }

        if (graph.isHorizontalSteps()) {
            graphHeight -= tickLength + innerPadding + ticksz;
        }
        if (graph.isVerticalSteps()) {
            graphX += tickLength+innerPadding+ticksz;
            graphWidth -= tickLength+innerPadding + ticksz;
        }

        paint.setColor(Color.LTGRAY);
        canvas.drawRect(graphX, graphY, graphX+graphWidth, graphY+graphHeight,paint);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        canvas.drawLine(graphX, graphY, graphX, graphY+graphHeight, paint);
        canvas.drawLine(graphX, graphY+graphHeight, graphX+graphWidth, graphY+graphHeight, paint);

        final float dx = graph.getMaxX() - graph.getMinX();
        final float dy = graph.getMaxY() - graph.getMinY();
        final float kx = (graphWidth - 2 * innerPadding) / dx;
        final float ky = - (graphHeight - 2*innerPadding) / dy;
        final float bx = graphX + innerPadding - kx * graph.getMinX();
        final float by = graphY + innerPadding - ky * graph.getMaxY();

        final int h = graph.getHash();
        if ((lx0 != graphX) || (lx1 != graphWidth) || (ly0 != graphY) || (ly1 != graphHeight) || (h != h1)) {
            values[0] = graph.getX(0) * kx + bx;
            values[1] = graph.getY(0) * ky + by;
            for (int i = 1; i < graph.getLength() -1; i++) {
                values[4*i] = graph.getX(i) * kx + bx;
                values[4*i-2] = values[4*i];
                values[4*i+1] = graph.getY(i) * ky + by;
                values[4*i-1] = values[4*i+1];
            }
            final int last =graph.getLength()-1;
            values[values.length -2] = graph.getX(last) * kx + bx;
            values[values.length -1] = graph.getY(last) * ky + by;
            lx0 = graphX; lx1 = graphWidth; ly0 = graphY; ly1 = graphHeight;
            h1 = h;
        }
        // show ticks
        if (graph.isHorizontalSteps()) {
            final int appi = graphWidth / tickfreq;
            final float appst = dx / appi;
            final double aplg = Math.log10(appst);
            int pw = (int)Math.floor(aplg);
            final double xp = Math.pow(10, (aplg - pw));
            final double step;
            int prec = -pw;
            if (xp <= 2) step = 2 * Math.pow(10, pw);
            else if (xp <= 5) step = 5 * Math.pow(10, pw);
            else {
                step = 10 * Math.pow(10, pw);
                prec = -(pw + 1);
            }
            final int ix = (int) Math.ceil(dx / step);
            if (prec < 0) prec=0;
            final float x0 = (float)  (step * Math.floor(graph.getMinX() / step));
            for (int i = 0; i < ix+2; i++) {
                final float x1 = (x0 + (float) step * i);
                final float xx = kx * x1 + bx;
                if (xx < graphX) continue;
                if (xx > graphX + graphWidth + 2*innerPadding) continue;
                canvas.drawLine(xx, graphY + graphHeight, xx,
                        graphY+graphHeight+tickLength, paint);
                canvas.drawText(String.format(Locale.ENGLISH,
                        "%."+Integer.toString(prec)+"f", x1), xx,
                        graphY+graphHeight+tickLength+innerPadding+ticksz, paint);
            }
        }

        paint.setTextAlign(Align.RIGHT);
        if (graph.isVerticalSteps()) {
            final int appi = graphHeight / tickfreq;
            final float appst = dy / appi;
            final double aplg = Math.log10(appst);
            int pw = (int)Math.floor(aplg);
            final double yp = Math.pow(10, (aplg - pw));
            final double step;
            int prec = -pw;
            if (yp <= 2) step = 2 * Math.pow(10, pw);
            else if (yp <= 5) step = 5 * Math.pow(10, pw);
            else {
                step = 10 * Math.pow(10, pw);
                prec = -(pw + 1);
            }
            final int iy = (int) Math.ceil(dy / step);
            if (prec < 0) prec=0;
            final float y0 = (float)  (step * Math.floor(graph.getMinY() / step));
            for (int i = 0; i < iy+2; i++) {
                final float y1 = (y0 + (float) step * i);
                final float yy = ky * y1 + by;
                if (yy < graphY) continue;
                if (yy > graphY + graphHeight + 2*innerPadding) continue;
                canvas.drawLine(graphX, yy,
                        graphX-tickLength, yy, paint);
                p.reset();
                p.setLastPoint(graphX-tickLength-innerPadding, yy+ticksz);
                p.lineTo(graphX-tickLength-innerPadding, yy-ticksz);
                canvas.drawTextOnPath(String.format(Locale.ENGLISH,
                        "%."+Integer.toString(prec)+"f", y1), p,0,0, paint);
            }
        }
        if (graph.getColor() != null)
            paint.setColor(graph.getColor());
        else
            paint.setColor(Color.RED);

        paint.setStrokeWidth(7);
        canvas.drawLines(values, paint);
    }
}
