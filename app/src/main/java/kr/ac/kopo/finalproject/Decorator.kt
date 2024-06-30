package kr.ac.kopo.finalproject

import android.app.Activity
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class Decorator(dates: Collection<CalendarDay>?, context: Activity) : DayViewDecorator {
    private val drawable: Drawable? = context.getDrawable(R.drawable.diary_check)
    private val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        drawable?.let {
            view.setSelectionDrawable(it)
        }
    }
}
