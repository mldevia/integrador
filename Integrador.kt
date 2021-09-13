import java.util.*
import kotlin.math.ceil

fun main(){

    val car = Vehicle("AA111AA", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_001")
    val moto = Vehicle("B222BBB", VehicleType.MOTORCYCLE, Calendar.getInstance())
    val minibus = Vehicle("CC333CC", VehicleType.MINIBUS, Calendar.getInstance())
    val bus = Vehicle("DD444DD", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_002")
    val car6 = Vehicle("AA111AA6", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_003")
    val moto2 = Vehicle("B222BBB2", VehicleType.MOTORCYCLE, Calendar.getInstance())
    val minibus2 = Vehicle("CC333CC2", VehicleType.MINIBUS, Calendar.getInstance())
    val bus2 = Vehicle("DD444DD2", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_004")
    val car3 = Vehicle("AA111AA3", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_005")
    val moto3 = Vehicle("B222BBB3", VehicleType.MOTORCYCLE, Calendar.getInstance())
    val minibus3 = Vehicle("CC333CC3", VehicleType.MINIBUS, Calendar.getInstance())
    val bus3 = Vehicle("DD444DD3", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_006")
    val car4 = Vehicle("AA111AA4", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_007")
    val moto4 = Vehicle("B222BBB4", VehicleType.MOTORCYCLE, Calendar.getInstance())
    val minibus4 = Vehicle("CC333CC4", VehicleType.MINIBUS, Calendar.getInstance())
    val bus4 = Vehicle("DD444DD4", VehicleType.BUS, Calendar.getInstance(), "DISCOUNT_CARD_008")
    val car5 = Vehicle("AA111AA8", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_009")
    val moto5 = Vehicle("B222BBB5", VehicleType.MOTORCYCLE, Calendar.getInstance())
    val minibus5 = Vehicle("CC333CC5", VehicleType.MINIBUS, Calendar.getInstance())
    val bus5 = Vehicle("CC333CC6", VehicleType.MINIBUS, Calendar.getInstance())
    val car7 = Vehicle("AA111AAh", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_010")
    val moto6 = Vehicle("B222BBBj", VehicleType.MOTORCYCLE, Calendar.getInstance())

    val parking = Parking(mutableSetOf())
    println(parking.vehicles.contains(car))
    println(parking.vehicles.contains(moto))
    println(parking.vehicles.contains(minibus))
    println(parking.vehicles.contains(bus))

    val car2 =Vehicle("AA111AA", VehicleType.CAR, Calendar.getInstance(), "DISCOUNT_CARD_011")
    val isCar2Inserted = parking.vehicles.add(car2)

    println(isCar2Inserted)

    parking.vehicles.remove(moto)

    val arrayVehicle= arrayOf(car3, car4, car5,car6, car7,
        moto2, moto3,moto4,moto5, moto6,
        minibus2, minibus3, minibus4, minibus5, bus2,
        bus3, bus4,bus5,moto, bus,
        minibus,car
    )

    arrayVehicle.forEach {
        if(parking.addVehicle(it)){
            println("Welcome to AlkeParking!")
        }else{
            println("Sorry, the check-in failed")
        }
    }

    parking.checkOutVehicle(car3.plate)
    parking.checkOutVehicle(car.plate)

    parking.showEarnings()
    parking.showPlates()

}
//Parking spot
interface Parkable{

    fun checkOutVehicle(plate: String)
    fun onSuccess(fee:Int)
    fun onError()
}

data class Parking(val vehicles: MutableSet<Vehicle>) : Parkable{
    var administracion = Pair(0,0)
    val maxVehicle = 20
    fun addVehicle(vehicle:Vehicle):Boolean{
        if(vehicles.size < maxVehicle){
            return vehicles.add(vehicle)
        }
        return false
    }

    override fun checkOutVehicle(plate: String) {
        val vehicle = vehicles.filter{ it.plate == plate}

        if(vehicle.isNullOrEmpty()){
            onError()
        }else{
            val tarifa = calculateFee(vehicle[0].type, vehicle[0].parkedTime, !vehicle[0].DiscountCard.isNullOrEmpty())
            onSuccess(tarifa)
            administracion= Pair(administracion.first+1, administracion.second+tarifa)
            vehicles.remove(vehicle[0])
        }
    }

    override fun onSuccess(fee: Int) {
        println("Your fee is \$$fee. Come back soon")
    }

    override fun onError() {
        println("Sorry, the check-out failed")
    }

    fun showEarnings(){
        println("${administracion.first} vehicles have checked out and have earnings of \$${administracion.second}")
    }

    fun showPlates(){
        vehicles.forEach { println(it.plate) }
    }

}

data class Vehicle (val plate:String, val type: VehicleType, val checkInTime:Calendar, val DiscountCard:String?=null){

    val parkedTime: Long
        get()= (Calendar.getInstance().timeInMillis - checkInTime.timeInMillis)/Constants.MINUTES_IN_MILISECONDS

    override fun equals(other: Any?): Boolean {
        if(other is Vehicle){
            return this.plate == other.plate
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return this.plate.hashCode()
    }

}

enum class VehicleType(val rate:Int){
    CAR(20),
    MOTORCYCLE( 15),
    MINIBUS(25),
    BUS(30)
}

class Constants(){
    companion object{
        const val MINUTES_IN_MILISECONDS = 60000
    }
}


fun calculateFee(type:VehicleType, parkedTime:Long, hasDiscountCard:Boolean):Int {


    var fee = when {
        parkedTime <= 120 -> type.rate
        else -> {
            val estacionadoSinDosHoras = (parkedTime - 120).toDouble()
            val bloques = ceil(estacionadoSinDosHoras / 15)
            var valor = bloques * 5
            valor.toInt() + type.rate
        }

    }


    if (hasDiscountCard) {
        fee = (fee * (0.85)).toInt()
    }

    return fee

}

