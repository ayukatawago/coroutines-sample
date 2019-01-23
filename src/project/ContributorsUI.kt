package project

import kotlinx.coroutines.*
import kotlinx.coroutines.debug.DebugProbes
import kotlinx.coroutines.swing.Swing
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.prefs.Preferences
import javax.swing.*
import javax.swing.table.DefaultTableModel
import kotlin.coroutines.CoroutineContext


@ExperimentalCoroutinesApi
fun main() {
    DebugProbes.install()
    setDefaultFontSize(18f)
    ContributorsUI().apply {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}

private val INSETS = Insets(3, 10, 3, 10)
private val COLUMNS = arrayOf("Login", "Contributions")

@Suppress("CONFLICTING_INHERITED_JVM_DECLARATIONS")
class ContributorsUI : JFrame("GitHub Contributors"), CoroutineScope {
    private val username = JTextField(20)
    private val password = JTextField(20)
    private val org = JTextField(20)
    private val variant = JComboBox<Variant>(Variant.values())
    private val load = JButton("Load contributors")
    private val cancel = JButton("Cancel").apply { isEnabled = false }

    private val resultsModel = DefaultTableModel(COLUMNS, 0)
    private val results = JTable(resultsModel)
    private val resultsScroll = JScrollPane(results).apply {
        preferredSize = Dimension(200, 200)
    }

    private val icon = ImageIcon(javaClass.classLoader.getResource("ajax-loader.gif"))
    private val animation = JLabel("Event thread is active", icon, SwingConstants.CENTER)

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Swing

    init {
        // Create UI
        rootPane.contentPane = JPanel(GridBagLayout()).apply {
            addLabeled("GitHub Username", username)
            addLabeled("Password/Token", password)
            addWideSeparator()
            addLabeled("Organization", org)
            addLabeled("Variant", variant)
            addWideSeparator()
            addWide(JPanel().apply {
                add(load)
                add(cancel)
            })
            addWide(resultsScroll) {
                weightx = 1.0
                weighty = 1.0
                fill = GridBagConstraints.BOTH
            }
            addWide(animation)
        }
        // Add button listener
        load.addActionListener {
            savePrefs()
            doLoad()
        }
        // Install window close listener to save preferences and exit
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                job.cancel()
                savePrefs()
                System.exit(0)
            }
        })
        // Load initial preferences
        loadPrefs()
    }

    private fun selectedVariant(): Variant = variant.getItemAt(variant.selectedIndex)

    private fun doLoad() {
        clearResults()
        val req = RequestData(username.text, password.text, org.text)
        when (selectedVariant()) {
            Variant.BLOCKING -> {
                // TODO Blocking UI thread
            }
            Variant.BACKGROUND -> {
                // TODO Blocking a background thread
            }
            Variant.CALLBACKS -> {
                // TODO Using callbacks
            }
            Variant.COROUTINE -> {
                // TODO Using coroutines
            }
            Variant.PROGRESS -> {
                // TODO Using coroutines showing progress
            }
            Variant.CANCELLABLE -> {
                // TODO Using coroutines with cancellation
            }
            Variant.CONCURRENT -> {
                // TODO concurrent implementation
            }
            Variant.FUTURE -> {
                // TODO future implementation
            }
            Variant.GATHER -> {
                // TODO gather implementation
            }
            Variant.ACTOR -> {
                // TODO actor implementation
            }
        }
    }

    private fun clearResults() {
        updateResults(listOf())
    }

    private fun updateResults(users: List<User>) {
        resultsModel.setDataVector(users.map {
            arrayOf(it.login, it.contributions)
        }.toTypedArray(), COLUMNS)
    }

    private fun updateEnabled(enabled: Boolean) {
        load.isEnabled = enabled
        cancel.isEnabled = !enabled
    }

    private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

    private fun loadPrefs() {
        prefNode().apply {
            username.text = get("username", "")
            password.text = get("password", "")
            org.text = get("org", "kotlin")
            variant.selectedIndex = variantOf(get("variant", "")).ordinal
        }
    }

    private fun savePrefs() {
        prefNode().apply {
            put("username", username.text)
            put("password", password.text)
            put("org", org.text)
            put("variant", selectedVariant().name)
            sync()
        }
    }
}

fun variantOf(str: String): Variant =
    try {
        Variant.valueOf(str)
    } catch (e: IllegalArgumentException) {
        Variant.values()[0]
    }

fun JPanel.addLabeled(label: String, component: JComponent) {
    add(JLabel(label), GridBagConstraints().apply {
        gridx = 0
        insets = INSETS
    })
    add(component, GridBagConstraints().apply {
        gridx = 1
        insets = INSETS
        anchor = GridBagConstraints.WEST
        fill = GridBagConstraints.HORIZONTAL
        weightx = 1.0
    })
}

fun JPanel.addWide(component: JComponent, constraints: GridBagConstraints.() -> Unit = {}) {
    add(component, GridBagConstraints().apply {
        gridx = 0
        gridwidth = 2
        insets = INSETS
        constraints()
    })
}

fun JPanel.addWideSeparator() {
    addWide(JSeparator()) {
        fill = GridBagConstraints.HORIZONTAL
    }
}