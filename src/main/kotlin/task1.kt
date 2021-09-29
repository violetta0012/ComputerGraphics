package my

import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.WritableImage
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.stage.Stage
import java.awt.Point

class Task1(primaryStage: Stage) {
    init {
        val root = FlowPane()
        val scene = Scene(root, 900.0, 500.0)
        val canvas = Canvas(600.0, 400.0)
        val context = canvas.graphicsContext2D

        root.children.add(canvas)

        primaryStage.title = "Lab3"
        primaryStage.scene = scene
        primaryStage.show()

        val group = ToggleGroup()
        val clearButton = ToggleButton("Очистить")
        val drawButton = ToggleButton("Рисовать")
        val fillButton = ToggleButton("Заливка")
        clearButton.toggleGroup = group
        drawButton.toggleGroup = group
        fillButton.toggleGroup = group
        root.children.addAll(clearButton, drawButton, fillButton)

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
                if (line.endX != 0.0 && line.endY != 0.0) {
                    context.stroke = Color.RED
                    context.strokeLine(line.startX, line.startY, line.endX, line.endY)
                }
            }
        }

        fillButton.setOnAction {
            root.setOnMouseClicked {
                val image = canvas.snapshot(null, null)
                val start = Point(it.sceneX.toInt(), it.sceneY.toInt())
                fillWithColor(context, start, Color.AQUA, image)
            }
        }
    }

    fun fillWithColor(gc: GraphicsContext, start: Point, color: Color, image: WritableImage) {
        val pixelReader = image.pixelReader
        val backgroundColor = pixelReader.getColor(start.x, start.y)
        if (backgroundColor == color)
            return

        if (start.x > 0 && start.x < gc.canvas.width && start.y > 0 && start.y < gc.canvas.height &&
                pixelReader.getColor(start.x, start.y) != color) {
            var leftX = start.x
            while (leftX - 1 > 0 && pixelReader.getColor(leftX - 1, start.y) == backgroundColor)
                leftX--
            var rightX = start.x
            while (rightX + 1 < gc.canvas.width.toInt() && pixelReader.getColor(rightX + 1, start.y) == backgroundColor)
                rightX++

            gc.stroke = color
            gc.strokeLine(leftX.toDouble(), start.y.toDouble(), rightX.toDouble(), start.y.toDouble());

            for (x in leftX until rightX) {
                if (start.y - 1 > 0 && pixelReader.getColor(x, start.y - 1) == backgroundColor)
                    fillWithColor(gc, Point(x, start.y - 1), color, image)
                if (start.y + 1 < gc.canvas.height && pixelReader.getColor(x, start.y + 1) == backgroundColor)
                    fillWithColor(gc, Point(x, start.y + 1), color, image)
            }
        }
    }
}