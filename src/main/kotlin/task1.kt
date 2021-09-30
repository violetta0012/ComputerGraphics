package my

import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Point
import java.io.File
import java.io.FileInputStream

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
        val imageButton = ToggleButton("Заливка изображением")
        clearButton.toggleGroup = group
        drawButton.toggleGroup = group
        fillButton.toggleGroup = group
        imageButton.toggleGroup = group
        root.children.addAll(clearButton, drawButton, fillButton, imageButton)

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
                    context.stroke = Color.GOLD
                    context.strokeLine(line.startX, line.startY, line.endX, line.endY)
                }
            }
        }

        fillButton.setOnAction {
            root.setOnMouseClicked {
                val image = canvas.snapshot(null, null)
                val start = Point(it.sceneX.toInt(), it.sceneY.toInt())
                fillWithColor(context, start, Color.CADETBLUE, image)
            }
        }

        imageButton.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.title = "Доступные файлы"
            fileChooser.initialDirectory = File("/Users/violettagerasimova/IdeaProjects/ComputerGraphics/src/Pictures")
            fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"))
            val selectedFile = fileChooser.showOpenDialog(primaryStage)
            root.setOnMouseClicked {
                val start = Point(it.sceneX.toInt(), it.sceneY.toInt())
                val image = canvas.snapshot(null, null)
                val picture = Image(FileInputStream(selectedFile))
                val pictureStart = Point(0, 0)
                fillWithPicture(context, start, picture, image)
            }
        }
    }

    private fun fillWithColor(gc: GraphicsContext, start: Point, color: Color, image: WritableImage) {
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

            val pixelWriter = image.pixelWriter
            for (x in leftX..rightX)
                pixelWriter.setColor(x, start.y, color)
            gc.drawImage(image, 0.0, 0.0)

            for (x in leftX..rightX) {
                if (start.y - 1 > 0 && pixelReader.getColor(x, start.y - 1) == backgroundColor)
                    fillWithColor(gc, Point(x, start.y - 1), color, image)
                if (start.y + 1 < gc.canvas.height && pixelReader.getColor(x, start.y + 1) == backgroundColor)
                    fillWithColor(gc, Point(x, start.y + 1), color, image)
            }
        }
    }

    private fun fillWithPicture(gc: GraphicsContext, start: Point, picture: Image, image: WritableImage) {
        fillWithPicture(gc, start, picture, image, Point(picture.width.toInt() / 2, picture.height.toInt() / 2))
    }

    private fun fillWithPicture(gc: GraphicsContext, start: Point, picture: Image, image: WritableImage, pictureStart: Point) {
        val imageReader = image.pixelReader
        val pictureReader = picture.pixelReader
        val backgroundColor = imageReader.getColor(start.x, start.y)
        val imageWriter = image.pixelWriter
        var picStart = Point(pictureStart.x, pictureStart.y)

        while (picStart.y < 0)
            picStart.y += picture.height.toInt()
        if (picStart.y >= picture.height)
            picStart.y %= picture.height.toInt()

        var leftX = start.x
        var leftPicStart = picStart.x
        while (leftX - 1 >= 0 && imageReader.getColor(leftX - 1, start.y) == backgroundColor) {
            leftX--
            leftPicStart--
        }
        var rightX = start.x
        while (rightX + 1 < gc.canvas.width.toInt() && imageReader.getColor(rightX + 1, start.y) == backgroundColor)
            rightX++

        picStart.x = leftPicStart - 1
        while (picStart.x < 0)
            picStart.x += picture.width.toInt()
        for (x in leftX..rightX) {
            picStart.x++
            if (picStart.x >= picture.width)
                picStart.x %= picture.width.toInt()
            imageWriter.setColor(x, start.y, pictureReader.getColor(picStart.x, picStart.y))
        }
        gc.drawImage(image, 0.0, 0.0)

        picStart.x = leftPicStart - 1
        for (x in leftX..rightX) {
            picStart.x++
            if (start.y - 1 > 0 && imageReader.getColor(x, start.y - 1) == backgroundColor)
                fillWithPicture(gc, Point(x, start.y - 1), picture, image, Point(picStart.x, picStart.y - 1))
            if (start.y + 1 < gc.canvas.height && imageReader.getColor(x, start.y + 1) == backgroundColor)
                fillWithPicture(gc, Point(x, start.y + 1), picture, image, Point(picStart.x, picStart.y + 1))
        }
    }
}