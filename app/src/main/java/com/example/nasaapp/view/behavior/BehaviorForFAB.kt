package com.example.nasaapp.view.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.nasaapp.R

class BehaviorForFAB(val context: Context, attrs: AttributeSet? = null):CoordinatorLayout.Behavior<View>(context, attrs) {

    // коэффициент для определения зависимости child.alfa от dependency.y
    private var cofAlfa:Float = 0.0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return  if (dependency.id== R.id.bottom_sheet_layout){
            child.x = dependency.x + dependency.width - context.resources.getDimension(R.dimen.margin_end_fab_back) - child.width
            if (cofAlfa==0.0f)
                cofAlfa = dependency.y
            true}
            else false
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency.id== R.id.bottom_sheet_layout)
            child.y = dependency.y - context.resources.getDimension(R.dimen.margin_bottom_fab_back)
        child.alpha = dependency.y/cofAlfa
        return super.onDependentViewChanged(parent, child, dependency)
    }

}