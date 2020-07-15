package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import lombok.Getter;
import lombok.Setter;
/**
 * Description:绘制线
 */
@Setter
@Getter
public class Lines extends DottedLine {
    private float dash;
    private float phase;
    private Boolean isDottedLine;

    @Override
    public void draw (PdfCanvas canvas, Rectangle drawArea) {
        //虚线
        if (isDottedLine != null && isDottedLine) {
            canvas.saveState ().setLineWidth (getLineWidth ()).setLineDash (dash, gap, phase).moveTo (drawArea.getX (), drawArea.getY ()).lineTo (drawArea.getX () + drawArea.getWidth (), drawArea.getY ()).stroke ().restoreState ();
        }
        //直线
        else {
            canvas.saveState ().setLineWidth (getLineWidth ()).moveTo (drawArea.getX (), drawArea.getY ()).lineTo (drawArea.getX () + drawArea.getWidth (), drawArea.getY ()).stroke ().restoreState ();
        }
    }
}
