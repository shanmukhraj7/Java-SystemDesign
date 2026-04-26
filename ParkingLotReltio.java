import java.util.*;

enum VehicleType{
    BIKE, CAR, TRUCK
}

enum SlotType{
    BIKE_SLOT, CAR_SLOT, LARGE_SLOT
}

abstract class Vehicle{
    protected String vehicleNumber;
    protected VehicleType type;

    public Vehicle(String vehicleNumber, VehicleType type){
        this.vehicleNumber = vehicleNumber;
        this.type = type;
    }

    public String getVehicleNumber(){
        return vehicleNumber;
    }

    public VehicleType getType(){
        return getType();
    }

    public abstract int getRate();
}

class Bike extends Vehicle{
    public Bike(String num){
        super(num, VehicleType.BIKE);
    }

    public int getRate(){
        return 10;
    }
}

class Car extends Vehicle{
    public Car(String num){
        super(num, VehicleType.CAR);
    }

    public int getRate(){
        return 20;
    }
}

class Truck extends Vehicle{
    public Truck(String num){
        super(num, VehicleType.TRUCK);
    }

    public int getRate(){
        return 30;
    }
}

class ParkingSlot{
    int slotNumber;
    SlotType type;
    Vehicle vehicle;

    public ParkingSlot(int slotNumber, SlotType type){
        this.slotNumber = slotNumber;
        this.type = type;
        this.vehicle = null;
    }

    public boolean isFree() {
        return vehicle == null;
    }

    public boolean canFit(Vehicle v){
        if(v.getType() == VehicleType.BIKE && type == SlotType.BIKE_SLOT) return true;
        if(v.getType() == VehicleType.CAR && type == SlotType.CAR_SLOT) return true;
        if(v.getType() == VehicleType.TRUCK && type == SlotType.LARGE_SLOT) return true;
        return false;
    }

    public void park(Vehicle v){
        this.vehicle = v;
    }

    public void unpark(){
        this.vehicle = null;
    }
}

class ParkingLot{
    private List<ParkingSlot> slots;

    public ParkingLot(int bikeSlots, int carSlots, int largeSlots){
        slots = new ArrayList<>();
        int id = 1;

        for(int i = 0; i < bikeSlots; i++){
            slots.add(new ParkingSlot(id++, SlotType.BIKE_SLOT));
        }

        for(int  i = 0; i < carSlots; i++){
            slots.add(new ParkingSlot(id++, SlotType.CAR_SLOT));
        }

        for(int i = 0; i < largeSlots; i++){
            slots.add(new ParkingSlot(id++, SlotType.LARGE_SLOT));
        }
    }

    public void park(Vehicle v){
        for(ParkingSlot slot : slots){
            if(slot.isFree() && slot.canFit(v)){
                slot.park(v);
                System.out.println("Vehicle " + v.getVehicleNumber() + " is parked at " + slot.slotNumber);
                return;
            }
        }
        System.out.println("No slots Available for vehicle " + v.getVehicleNumber());
    }

    public void unpark(String vehicleNumber, int hours){
        for(ParkingSlot slot : slots){
            if(!slot.isFree() && slot.vehicle.getVehicleNumber().equals(vehicleNumber)){
                int fee = slot.vehicle.getRate() * hours;
                System.out.println("Vehicle " + vehicleNumber + " removed.");
                System.out.println("Parking Fee: ₹" + fee);
                slot.unpark();
                return;
            }
        }
        System.out.println("Vehicle not found");
    }

    public void displayStatus() {
        int bikeFree = 0, carFree = 0, largeFree = 0;

        for (ParkingSlot slot : slots) {
            if (slot.isFree()) {
                if (slot.type == SlotType.BIKE_SLOT) bikeFree++;
                else if (slot.type == SlotType.CAR_SLOT) carFree++;
                else largeFree++;
            }
        }
        System.out.println("\n--- Parking Status ---");
        System.out.println("Bike Slots Free: " + bikeFree);
        System.out.println("Car Slots Free: " + carFree);
        System.out.println("Truck Slots Free: " + largeFree);
    }
}


public class ParkingLotReltio {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(2, 2, 1);
        lot.park(new Bike("B1"));
        lot.park(new Car("C1"));
        lot.park(new Truck("T1"));
        lot.park(new Car("C2"));
        lot.park(new Bike("B2"));
        lot.displayStatus();
        lot.unpark("C1", 3);
        lot.unpark("B1", 2);
        lot.displayStatus();
    }
}
