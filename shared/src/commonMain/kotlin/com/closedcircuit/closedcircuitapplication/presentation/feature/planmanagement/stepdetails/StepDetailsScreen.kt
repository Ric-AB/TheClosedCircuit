@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberCalendarMonth
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import org.koin.core.component.KoinComponent

internal object StepDetailsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
    BaseScaffold(topBar = { StepDetailsAppBar(goBack) }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Conduct Market Research",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
            )

            StepSummary()
            BodyText(
                text = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit. Exercitation veniam consequat...",
                modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Budget items", style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = defaultHorizontalScreenPadding)
            )

            Spacer(modifier = Modifier.height(8.dp))
            repeat(5) {
                BudgetItem(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    "Cost of access to data",
                    "4,000",
                    "2,000"
                )
            }
        }
    }
}

@Composable
private fun StepSummary() {
    @Composable
    fun Item(imageVector: ImageVector, text: String, contentDescription: String? = null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(100.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription
            )

            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
    ) {
        val dividerModifier = Modifier.fillMaxHeight(.5f).width(1.dp)
        Item(
            imageVector = rememberCalendarMonth(),
            text = "3 Months"
        )

        Divider(modifier = dividerModifier)

        Item(
            imageVector = rememberCalendarMonth(),
            text = "NGN 3,000"
        )

        Divider(modifier = dividerModifier)

        Item(
            imageVector = rememberCalendarMonth(),
            text = "NGN 4,000"
        )
    }
}

@Composable
private fun StepDetailsAppBar(onNavClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "navigation icon")
            }
        },
        title = {},
        actions = {
            StepDetailsAppBarActions({}, {})
        }
    )
}

@Composable
private fun RowScope.StepDetailsAppBarActions(
    onDeleteIconClick: () -> Unit,
    onEditIconClick: () -> Unit
) {
    IconButton(onClick = onEditIconClick) {
        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit step")
    }

    IconButton(onClick = onDeleteIconClick) {
        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "edit step")
    }

    DropDownMenu()
}

@Composable
private fun DropDownMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete step") },
                onClick = { }
            )
        }
    }
}