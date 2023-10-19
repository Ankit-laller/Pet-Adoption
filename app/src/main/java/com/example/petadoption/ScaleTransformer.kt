import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class ScaleTransformer(private val scalingValue: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val offset = parent.width * (1 - scalingValue) / 2
        outRect.set(offset.toInt(), 0, offset.toInt(), 0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            child.scaleY = scalingValue
            child.scaleX = scalingValue
        }
    }
}
