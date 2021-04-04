package card_reader.data_capture

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import com.github.sarxos.webcam.WebcamResolution
import com.github.sarxos.webcam.util.jh.JHFlipFilter
import common.PATH_PREFIX
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import java.time.LocalDateTime
import javax.imageio.ImageIO
import javax.swing.JFrame


fun main() {
  val webcam = Webcam.getDefault()!!

  webcam.setCustomViewSizes(WebcamResolution.UHD4K.size)
  webcam.viewSize = WebcamResolution.UHD4K.getSize()

  webcam.open()

  val panel = WebcamPanel(webcam)
  panel.painter = PanelHelper.getPainter(panel)
  panel.isFPSDisplayed = true
  panel.isDisplayDebugInfo = true
  panel.isImageSizeDisplayed = true
  panel.isMirrored = false

  val window = MyFrame("Webcam", webcam)
  window.add(panel)
  window.isFocusable = true
  window.focusTraversalKeysEnabled = false
  window.isResizable = true
  window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  window.pack()
  window.isVisible = true
}

class MyFrame(title: String, private val webcam: Webcam): JFrame(title), KeyListener {
  private val rotate = JHFlipFilter(JHFlipFilter.FLIP_90CW)

  init {
    addKeyListener(this)
  }

  override fun keyTyped(e: KeyEvent?) {
    val time = LocalDateTime.now().toString()
        .replace(':', '_')
        .replace('/', '_')
        .replace('.', '_')

    val image = rotate.filter(
        webcam.image.getSubimage(0,0,webcam.image.width - 450, webcam.image.height),
        null)
    ImageIO.write(image, "jpg", File("${PATH_PREFIX}card_pictures/initial/image_$time.jpg"))
  }

  override fun keyPressed(e: KeyEvent?) {
  }

  override fun keyReleased(e: KeyEvent?) {
  }

}