package com.example.ex01_opengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet.Layout

class MainActivity : AppCompatActivity() {

	var myLayout: Layout? = null
	var myGLView: GLSurfaceView ? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)


		myGLView = findViewById(R.id.myGLView)

		myGLView!!.setEGLContextClientVersion(3)
		//일시중지시 EGLContext 유지여부
		myGLView!!.preserveEGLContextOnPause=true

		// 어떻게 그릴 것인가
		myGLView!!.setRenderer(MyGLRenderer())

		// 화면 렌더링을 언제 할 것이가 = 렌더러 반복호출하여 장면을 다시 그린다.
		myGLView!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
	}

	override fun onResume() {
		super.onResume()
		myGLView!!.onResume()
	}

	override fun onPause() {
		super.onPause()
		myGLView!!.onPause()
	}

}