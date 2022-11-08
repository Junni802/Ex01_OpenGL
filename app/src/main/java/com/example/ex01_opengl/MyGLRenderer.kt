package com.example.ex01_opengl

import android.app.appsearch.SearchResult.MatchInfo
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

//GLSurfaceView 를 랜더럴하는 클래스
class MyGLRenderer: GLSurfaceView.Renderer {

	var mViewMatrix = FloatArray(16)
	var mProiMatrix = FloatArray(16)
	var mMVPMatrix = FloatArray(16)

	var arr = arrayListOf(floatArrayOf(
		-0.5f,0.5f,0f,  //top left
		-0.5f,-0.5f,0f,  //bottom left
		0.5f,-0.5f,0f,  //bottom right
		0.5f,0.5f,0f    //top right
	), floatArrayOf(
		0.5f,0.5f,0f,    //top right
		0.5f,-0.5f,0f,  //bottom right
		0.5f,-0.5f,-1f,
		0.5f,0.5f, -1f,
	), floatArrayOf(
		-0.5f,0.5f,0f,  //top left
		0.5f,0.5f,0f,    //top right
		0.5f,0.5f, -1f,
		-0.5f,0.5f,-1f
	))

	var arrColor = arrayListOf(floatArrayOf(0f, 1f, 0f, 1f), floatArrayOf(0f, 0f, 1f, 1f), floatArrayOf(1f, 0f, 0f, 1f))


	override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
		GLES30.glClearColor(1f, 0.6f, 0.6f, 100f)
		Log.d("MyGLRenderer 여", "onSurfaceCreated")
	}
	// 화면크기가 변경시 화면 크기를 가져와 작업
	override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
		Log.d("MyGLRenderer 여", "onSurfaceChanged")
		GLES30.glViewport(0, 0, width, height)

		var ratio = width.toFloat()/height

		// 프로젝션 Matrix 설정
		Matrix.frustumM(mProiMatrix, 0,
				-ratio, ratio,
			-1f,
			1f,
			1f,
			7f
		)
	}

	override fun onDrawFrame(p0: GL10?) {
		Log.d("MyGLRenderer 여", "onDrawFrame")
		GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

		// 카메라 Matrix 설정
		Matrix.setLookAtM(mViewMatrix, 0,
			2.5f, 2f, 0.5f,
			0f, 0f, 0f,
			0f, 1f, 0f
		)

		Matrix.multiplyMM(mMVPMatrix, 0, mProiMatrix, 0, mViewMatrix, 0)

		for(i in arr.indices) {

			var square = Square(
				arr[i]
			)

			square.mMVPMatrix = mMVPMatrix

			square.color = arrColor[i]


			square.draw()

		}

	}
}