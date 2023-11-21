
drop table Trip;
CREATE TABLE Trip (
    TripNumber INT PRIMARY KEY,
    StartLocationName VARCHAR(255),
    DestinationName VARCHAR(255)
);
Select * from Trip;

drop table Bus;
CREATE TABLE Bus (
    BusID INT PRIMARY KEY,
    Model VARCHAR(255),
    Year INT
);
Select * from Bus;

drop table TripOffering;
CREATE TABLE TripOffering (
    TripNumber INT,
    Date DATE,
    ScheduledStartTime TIME,
    ScheduledArrivalTime TIME,
    DriverName VARCHAR(255),
    BusID INT,
    PRIMARY KEY (TripNumber, Date, ScheduledStartTime),
    FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber),
    FOREIGN KEY (BusID) REFERENCES Bus(BusID)
);
Select * from TripOffering;

drop table Driver;
CREATE TABLE Driver (
    DriverName VARCHAR(255) PRIMARY KEY,
    DriverTelephoneNumber VARCHAR(20)
);
Select * from Driver;

drop table Stop;
CREATE TABLE Stop (
    StopNumber INT PRIMARY KEY,
    StopAddress VARCHAR(255)
);
Select * from Stop;

drop table ActualTripStopInfo;
CREATE TABLE ActualTripStopInfo (
    TripNumber INT,
    Date DATE,
    ScheduledStartTime TIME,
    StopNumber INT,
    ScheduledArrivalTime TIME,
    ActualStartTime TIME,
    ActualArrivalTime TIME,
    NumberOfPassengerIn INT,
    NumberOfPassengerOut INT,
    PRIMARY KEY (TripNumber, Date, ScheduledStartTime, StopNumber),
    FOREIGN KEY (TripNumber, Date, ScheduledStartTime) REFERENCES TripOffering(TripNumber, Date, ScheduledStartTime),
    FOREIGN KEY (StopNumber) REFERENCES Stop(StopNumber)
);
Select * from ActualTripStopInfo;

drop table TripStopInfo;
CREATE TABLE TripStopInfo (
    TripNumber INT,
    StopNumber INT,
    SequenceNumber INT,
    DrivingTime TIME,
    PRIMARY KEY (TripNumber, StopNumber),
    FOREIGN KEY (TripNumber) REFERENCES Trip(TripNumber),
    FOREIGN KEY (StopNumber) REFERENCES Stop(StopNumber)
);
Select * from TripStopInfo;
