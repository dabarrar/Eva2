package com.example.eva2



import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.eva2.DB.AppDatabase
import com.example.eva2.DB.Lista
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

 lifecycleScope.launch( Dispatchers.IO ) {
      val listaDao = AppDatabase.getInstance(this@MainActivity).listaDao()
       val cantRegistros = listaDao.contar()
       if ( cantRegistros < 1) {
           listaDao.insertar(Lista(0, "leche", false))
           listaDao.insertar(Lista(1, "Huevo", false))
           listaDao.insertar(Lista(2, "Carne", false))
           listaDao.insertar(Lista(3, "verduras", false))
           listaDao.insertar(Lista(4, "arroz", false))

       }
   }

   setContent {

       ListaCompraUI()

       }
   }

}

@Composable

fun ListaCompraUI(){
val contexto = LocalContext.current
val (listas, setListas) = remember  { mutableStateOf(emptyList<Lista>()) }

LaunchedEffect(listas){
   withContext(Dispatchers.IO) {
       val dao = AppDatabase.getInstance(contexto).listaDao()
       setListas (dao.findAll())
   }

}


LazyColumn(
   modifier = Modifier.fillMaxSize(),
){
    items(listas){     lista ->
        ListaIntemUI(lista) {
            setListas(emptyList<Lista>())
        }
   }
   item {
       //Boton agregar que lleva a AgregarActivity
       Button(
           onClick = {
               val intent = Intent(contexto, AgregarActivity::class.java)
               contexto.startActivity(intent)

           },
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp)
               
       ) {
           Text(text = stringResource(id = R.string.add_product_title), color = Color.White)
       }
   }
}
}

@Composable
fun ListaIntemUI(lista: Lista, onSave:()-> Unit = {}) {
val contexto = LocalContext.current
val alcanceCorrutina = rememberCoroutineScope()

Row (
   modifier = Modifier
       .fillMaxWidth()
       .padding(vertical = 20.dp, horizontal = 20.dp)
       .background(color = Color.LightGray)
       .padding(16.dp)
){
   if(lista.comprado){
   //icono para producto comprado
   Icon(
        Icons.Filled.Check,
       contentDescription = "Producto Comprado",
       tint = Color.Green,
       modifier = Modifier
           .size(32.dp)
           .clickable {
               alcanceCorrutina.launch(Dispatchers.IO) {
                   val dao = AppDatabase
                       .getInstance(contexto)
                       .listaDao()
                   lista.comprado = false
                   dao.actualizar(lista)
                   onSave()
               }
           }
   )}
   else {
   //Icono carro de compras para agregar
       Icon(
           Icons.Filled.ShoppingCart,
           contentDescription = "Producto No Comprado",
           tint = Color.Black,
           modifier = Modifier
               .size(32.dp)
               .clickable {
                   alcanceCorrutina.launch(Dispatchers.IO) {
                       val dao = AppDatabase
                           .getInstance(contexto)
                           .listaDao()
                       lista.comprado = true
                       dao.actualizar(lista)
                       onSave()
                   }
               }
       ) }
   Spacer(modifier = Modifier.width(16.dp))

   Text(
       text = lista.lista,
       modifier = Modifier
           .weight(2f)
           .align(Alignment.CenterVertically), style = TextStyle(
               fontSize = 16.sp,
               fontWeight = FontWeight.Bold,
               color = Color.Black
           )
   )
   // Icono eliminar tarea
   Icon(
       Icons.Filled.Delete,
       contentDescription = "Eliminar Compra",
       tint = Color.Red,
       modifier = Modifier
           .size(32.dp)
           .clickable {
               alcanceCorrutina.launch(Dispatchers.IO) {
                   val dao = AppDatabase
                       .getInstance(contexto)
                       .listaDao()
                   dao.eliminar(lista)
                   onSave()
               }
           }
   )
}
}
@Preview
@Composable
fun listaItemUIPreview(){
val lista = Lista(1,"tomate", true)
ListaIntemUI(lista)

}



