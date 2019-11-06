package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strings"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/hyperledger/fabric/protos/peer"
)

// Doc writes string to the blockchain (as JSON object) for a specific key
type Doc struct {
	ApplicationId  string `json:"applicaionid"`
	DeviceId       string `json:"deviceid"`
	HardwareSerial string `json:"hardwareserial"`
	Port           string `json:"port"`
	Counter        string `json:"counter"`
	PayloadRaw     string `json:"payloadraw"`
	Payload        string `json:"payload"`
	Time           string `json:"time"`
	DownloadUrl    string `json:"downloadurl"`
}

// ToJSON function converts the doc into a JSON object
func (doc *Doc) ToJSON() []byte {
	jsonDoc, _ := json.Marshal(doc)
	return jsonDoc
}

//=================================================================================================
//================================================================================= RETURN HANDLING
// Return handling: for return, we either return "shim.Success (payload []byte) with HttpRetCode=200"
// or "shim.Error(doc string) with HttpRetCode=500". However, we want to set our own status codes to
// map into HTTP return codes. A few utility functions:

// Success handling
func Success(rc int32, doc string, payload []byte) peer.Response {
	return peer.Response{
		Status:  rc,
		Message: doc,
		Payload: payload,
	}
}

// Error handling
func Error(rc int32, doc string) peer.Response {
	logger.Errorf("Error %d = %s", rc, doc)
	return peer.Response{
		Status:  rc,
		Message: doc,
	}
}

// Validate : all arguments for a function call is passed as a string array args[]. Validate that
// the number, type and length of the arguments are correct. Only string validations are supported
// here, as all parameters are strings of different lengths.
func Validate(funcName string, args []string, desc ...interface{}) peer.Response {

	logger.Debugf("Function: %s(%s)", funcName, strings.TrimSpace(strings.Join(args, ",")))

	// validation can be done here
	return Success(0, "OK", nil)

}

//=================================================================================================
//============================================================================================ MAIN
// Main function starts up the chaincode in the container during instantiate
//
var logger = shim.NewLogger("chaincode")

// LoraBlock struct
type LoraBlock struct {
}

func main() {
	if err := shim.Start(new(LoraBlock)); err != nil {
		fmt.Printf("Main: Error starting chaincode: %s", err)
	}
}

// Init is called during Instantiate transaction after the chaincode container
// has been established for the first time, allowing the chaincode to
// initialize its internal data. Note that chaincode upgrade also calls this
// function to reset or to migrate data, so be careful to avoid a scenario
// where you inadvertently clobber your ledger's data!
//
func (cc *LoraBlock) Init(stub shim.ChaincodeStubInterface) peer.Response {
	// Validate supplied init parameters, in this case zero arguments!
	if _, args := stub.GetFunctionAndParameters(); len(args) > 0 {
		return Error(http.StatusBadRequest, "Init: Incorrect number of arguments; no arguments were expected.")
	}
	return Success(http.StatusOK, "OK", nil)
}

// Invoke is called to update or query the ledger in a proposal transaction.
// Updated state variables are not committed to the ledger until the
// transaction is committed.
//
func (cc *LoraBlock) Invoke(stub shim.ChaincodeStubInterface) peer.Response {

	// Which function is been called?
	function, args := stub.GetFunctionAndParameters()

	// Route call to the correct function
	switch function {
	case "read":
		return cc.read(stub, args)
	case "create":
		return cc.create(stub, args)
	default:
		logger.Warningf("Invoke('%s') invalid!", function)
		return Error(http.StatusNotImplemented, "Invalid method! Valid methods are 'create|read'!")
	}
}

//=================================================================================================
//============================================================================================ READ
// Read by ID
//
func (cc *LoraBlock) read(stub shim.ChaincodeStubInterface, args []string) peer.Response {

	// Validate and extract parameters
	if rc := Validate("read", args /*args[0]=id*/, "%s", 1, 64); rc.Status > 0 {
		return rc
	}
	id := strings.ToLower(args[0])

	// Read the value for the ID
	if value, err := stub.GetState(id); err == nil && value != nil {
		return Success(http.StatusOK, "OK", value)
	}

	return Error(http.StatusNotFound, "Not Found")
}

//=================================================================================================
//========================================================================================== CREATE
// Creates Car Charging usage entry in blockchain
//
func (cc *LoraBlock) create(stub shim.ChaincodeStubInterface, args []string) peer.Response {

	// Validate and extract parameters
	if rc := Validate("create", args /*args[0]=id*/, "%s", 1, 64 /*args[1]=applicaionid*/, "%s", 1, 64 /*args[2]=deviceid*/, "%s", 1, 64 /*args[3]=hardwareserial*/, "%s", 1, 64 /*args[4]=port*/, "%s", 1, 64 /*args[4]=counter*/, "%s", 1, 64 /*args[4]=payloadraw*/, "%s", 1, 64 /*args[4]=payload*/, "%s", 1, 64 /*args[4]=time*/, "%s", 1, 64 /*args[4]=downloadurl*/, "%s", 1, 255); rc.Status > 0 {
		return rc
	}
	id := strings.ToLower(args[0])
	doc := &Doc{
		ApplicationId: 	args[1],
		DeviceId:  		args[2],
		HardwareSerial: args[3],
		Port:    		args[4],
		Counter: 		args[5],
		PayloadRaw:  	args[6],
		Payload:     	args[7],
		Time:    		args[8],
		DownloadUrl:    args[9]}

	// Write the message
	if err := stub.PutState(id, doc.ToJSON()); err != nil {
		return Error(http.StatusInternalServerError, err.Error())
	}

	stub.SetEvent("New Block Added", doc.ToJSON())

	return Success(http.StatusCreated, args[2], nil)
}
