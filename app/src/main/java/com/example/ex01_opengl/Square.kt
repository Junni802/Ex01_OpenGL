package com.example.ex01_opengl

import android.opengl.GLES30
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Square(vertexs: FloatArray) {
	var mMVPMatrix:FloatArray = FloatArray(0)

	//점 정보
	/*
	* -0.5,0.5,0    0.5,0.5,0
	*           ┌---┐
	*           └---┘
	* -0.5,-0.5,0   0.5,-0.5,0
	* */

	//var drawOrder1 = shortArrayOf(0,1,2,   	0,2,3)
	//var drawOrder2 = shortArrayOf(3,4,2,	3,4,5)
	//var drawOrder3 = shortArrayOf(3,6,5,	3,6,0)

	var drawOrder = shortArrayOf(0,1,2,   	0,2,3)


	//GPU로 그리기위한 함수 문자열로 작성
	//점 위치 환산
	var vertexShaderString = """
        uniform mat4 uMVPMatrix;
        attribute vec4 aPosition;
        void main(){
            gl_Position = uMVPMatrix * aPosition;
        }
    """.trimIndent()

	var fragmentShaderString = """
        precision mediump float;
        uniform vec4 vColor;
        void main(){
            gl_FragColor = vColor;
        }
    """.trimIndent()

	var vertexBuffer:FloatBuffer
	var drawBuffer:ShortBuffer
	var mProgram:Int
	var color = FloatArray(0)


	init{
		var vv = ByteBuffer.allocateDirect(vertexs.size*4)
		vv.order(ByteOrder.nativeOrder())
		vertexBuffer = vv.asFloatBuffer() //크기 지정
		vertexBuffer.put(vertexs)
		vertexBuffer.position(0)


		vv = ByteBuffer.allocateDirect(drawOrder.size*2)
		vv.order(ByteOrder.nativeOrder())
		drawBuffer = vv.asShortBuffer() //크기 지정
		drawBuffer.put(drawOrder)
		drawBuffer.position(0)



		//계산식을 ID로 부여하여 결합한다.
		val vertexID = loadShader(GLES30.GL_VERTEX_SHADER,vertexShaderString)

		val fragmentID = loadShader(GLES30.GL_FRAGMENT_SHADER,fragmentShaderString)

		mProgram = GLES30.glCreateProgram()
		GLES30.glAttachShader(mProgram, vertexID)
		GLES30.glAttachShader(mProgram, fragmentID)
		GLES30.glLinkProgram(mProgram)

	}

	fun loadShader(type:Int, shaderCode:String):Int{
		var res = GLES30.glCreateShader(type)
		GLES30.glShaderSource(res, shaderCode)
		GLES30.glCompileShader(res)
		return res
	}

	//그리기
	fun draw(){
		GLES30.glUseProgram(mProgram)
		var aPosition = GLES30.glGetAttribLocation(mProgram,"aPosition")
		//배열형태로 사용하기
		GLES30.glEnableVertexAttribArray(aPosition)

		GLES30.glVertexAttribPointer(
			aPosition,
			3 , // 한 점에 사용되는 갯수
			GLES30.GL_FLOAT,
			false,
			3*4,
			vertexBuffer
		)

		var vColor = (GLES30.glGetUniformLocation(mProgram,"vColor"))
		GLES30.glUniform4fv(vColor, 1,color,0)


		var uMVPMatrix = GLES30.glGetUniformLocation(mProgram,"uMVPMatrix")

		GLES30.glUniformMatrix4fv(uMVPMatrix,1,false,mMVPMatrix,0)

		GLES30.glUniform4fv(vColor, 1,color,0)

		//객체를 그린다.
		GLES30.glDrawElements(
			GLES30.GL_TRIANGLES,
			drawOrder.size,
			GLES30.GL_UNSIGNED_SHORT,
			drawBuffer
		)
		//배열로 점 지정 해제
		GLES30.glDisableVertexAttribArray(aPosition)
	}
}