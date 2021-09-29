package my

import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.FlowPane
import javafx.scene.shape.Line
import javafx.stage.Stage

class Task1(primaryStage: Stage) {
    init {
        val root = FlowPane()
        val scene = Scene(root, 800.0, 500.0)
        val canvas = Canvas(600.0, 400.0)
        val context = canvas.graphicsContext2D

        root.children.add(canvas)

        primaryStage.title = "Lab3"
        primaryStage.scene = scene
        primaryStage.show()

        val group = ToggleGroup()
        val clearButton = ToggleButton("Очистить")
        val drawButton = ToggleButton("Рисовать")
        clearButton.toggleGroup = group
        drawButton.toggleGroup = group
        root.children.addAll(clearButton, drawButton)
        var line = Line(0.0, 0.0, 0.0, 0.0)

        clearButton.setOnAction {
            context.clearRect(0.0, 0.0, canvas.width, canvas.height)
            line = Line(0.0, 0.0, 0.0, 0.0)
        }

        drawButton.setOnAction {
            root.setOnMouseClicked {
                line.endX = line.startX
                line.endY = line.startY
                line.startX = it.sceneX
                line.startY = it.sceneY
                if (line.endX != 0.0 && line.endY != 0.0)
                    context.strokeLine(line.startX, line.startY, line.endX, line.endY)
            }
        }
    }

}