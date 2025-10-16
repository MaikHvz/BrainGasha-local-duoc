package com.proyecto.braingasha

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.braingasha.ui.theme.CelesteFondo
import com.proyecto.braingasha.ui.theme.FondoBlanco
import com.proyecto.braingasha.ui.theme.Gris
import com.proyecto.braingasha.ui.theme.Negro
import com.proyecto.braingasha.ui.theme.Purpura
import com.proyecto.braingasha.ui.theme.Rosado
import org.w3c.dom.Text

@Composable
fun ColumnContainer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

    }

}

@Composable
fun  HomeScreen() {
    Surface(

        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        color = Color.White

    ) {
        Column (modifier=Modifier.padding(top = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Row ( horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .background(CelesteFondo).padding(all = 15.dp)){
                Row (

                    verticalAlignment = Alignment.CenterVertically,

                ){
                    
                    Image(

                        painter = painterResource(id = R.drawable.unicorn),
                        contentDescription = "Mi ícono personalizado",
                        modifier = Modifier.size(45.dp)



                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                    text="BrainGasha",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Purpura,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                }
                Row(

                    verticalAlignment=Alignment.CenterVertically
                ){
                    Image(
                        painter= painterResource(id=R.drawable.coin),
                        contentDescription="Mi icono de coin",
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text="900",
                        color = Purpura,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.heropet),
                    contentDescription = "Hero pet image",
                    modifier = Modifier.size(330.dp)
                )
                Text(
                    text = "Colecciona Tus",
                    color = Negro,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 45.sp
                )
                Text(
                    text = "Cartas Epicas",
                    color = Rosado,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 45.sp

                )
                Text(
                    text = "Obtén cartas únicas, amplía tu colección y conviértete en un maestro",
                    fontWeight = FontWeight.SemiBold,
                    color = Gris,

                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,        // centra el texto dentro de su bloque
                    modifier = Modifier
                        .width(280.dp)

                )

            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                    .background(CelesteFondo)) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 20.dp)

                    ) { Text(text = "1",
                                fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Purpura,
                        fontSize = 22.sp)

                            Text(text = "Cartas \n" +
                                    "Coleccionadas",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Gris,
                                fontSize = 15.sp)

                    }
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.padding(top = 20.dp))
                    {

                        Text(text = "1"
                            ,fontWeight = FontWeight.ExtraBold,
                             textAlign = TextAlign.Center,
                            color = Rosado,
                            fontSize = 22.sp)


                        Text(text = "Tiradas\n Totales",
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = Gris,
                            fontSize = 15.sp)}
                }

                Button(
                    onClick = { /* acción al presionar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purpura,  // fondo del botón
                        contentColor = Color.White  // color del texto/icono
                    ),
                    shape = RoundedCornerShape(50), // bordes redondeados
                    modifier = Modifier
                        .fillMaxWidth(0.8f)   // ancho adaptable
                        .height(40.dp)
                ) {
                    Text(
                        text = "Tirar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedButton(
                    onClick = { /* acción */ },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(40.dp),
                    border = BorderStroke(2.dp, Purpura), // borde personalizado
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent, // fondo transparente
                        contentColor = Purpura              // color del texto
                    )
                ) {
                    Text(
                        text = "Ver Coleccion",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }


            }

            NavigationBar(
                containerColor = Color.White, // fondo
                modifier = Modifier.fillMaxWidth()


            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(100.dp) // tamaño
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sparkles),
                        contentDescription = "Home",
                        modifier = Modifier.size(100.dp) // tamaño
                    )
                    Image(
                        painter = painterResource(id = R.drawable.grid),
                        contentDescription = "Home",
                        modifier = Modifier.size(100.dp) // tamaño
                    )
                    Image(
                        painter = painterResource(id = R.drawable.shop),
                        contentDescription = "Home",
                        modifier = Modifier.size(100.dp) // tamaño
                    )


                }

            }
        }

    }
}





@Preview(showSystemUi = true)
@Composable
fun ColumnContainerPreview() {
    HomeScreen()

}