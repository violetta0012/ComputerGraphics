package my

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import java.awt.event.MouseEvent


class MyApplication : Application() {

    override fun start(primaryStage: Stage) {
        val task1 = Task1(primaryStage)

    }

}

fun main(args: Array<String>) {
    Application.launch(MyApplication::class.java, *args)
}