package com.ossnetwork.choloeksatheserver.controller;

import com.ossnetwork.choloeksatheserver.model.CarInfo;
import com.ossnetwork.choloeksatheserver.model.CarType;
import com.ossnetwork.choloeksatheserver.model.response.*;
import com.ossnetwork.choloeksatheserver.repository.CarInfoRepository;
import com.ossnetwork.choloeksatheserver.repository.CarTypeRepository;
import com.ossnetwork.choloeksatheserver.utils.CommonConstant;
import com.ossnetwork.choloeksatheserver.utils.CommonTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/car_info")
public class CarController {

    @Autowired
    CarInfoRepository carInfoRepository;
    @Autowired
    CarTypeRepository carTypeRepository;
    @Autowired
    ServletContext servletContext;

    /**
     * This function will save a new car information into database
     * @param carInfo
     * @return CarSetupResponse
     */
    @PostMapping(value = "/setup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CarSetupResponse carOwnerCarInfoSetup(@RequestBody CarInfo carInfo){
        try {
            // set car image into local directory (project directory)
            if(!carInfo.getImage().isEmpty()){

                File file = File.createTempFile("CAR_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+"_",".jpg", CommonTask.getImageDirectory(servletContext));
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(Base64.getDecoder().decode(carInfo.getImage()));
                fos.close();

                // now change image string to path
                carInfo.setImage("http://"+CommonTask.getIP()+":"+CommonConstant.PORT+"/cholo-ek-sathe/"+CommonConstant.BASE_IMAGE_PATH+file.getName());
            }
            carInfo.setStartDate(new Timestamp(new Date().getTime()));

            // save car info with picture url
            CarInfo newCar = carInfoRepository.save(carInfo);
            if(newCar != null)
                return new CarSetupResponse(HttpStatus.OK.value(), "Car setup successfully completed.", true, newCar.getCarInfoId());
        }catch (Exception ex){
            return new CarSetupResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false, -1);
        }
        return new CarSetupResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Car setup failed", false, -1);
    }


    /**
     * This function will send all car type information
     * @return List of car type (CarTypeList)
     */
    @GetMapping(value = "/carTypeList")
    public CarTypeList getAllCarType(){
        CarTypeList carTypeList;
        List<CarType> carTypes = carTypeRepository.findAll();
        if(carTypes != null && carTypes.size()>0){
            carTypeList = new CarTypeList(HttpStatus.OK.value(), "Success", true, carTypes);
        }else{
            carTypeList = new CarTypeList(HttpStatus.NOT_FOUND.value(), "failed", false, null);
        }

        return carTypeList;

    }

    /**
     * this function will send your car information based on given id
     * @param carid
     * @return CarInfo
     */
    @GetMapping(value = "/car/{id}")
    public CarInfoByCarIdResponseRoot getCarInfoByCarId(@PathVariable(value = "id") int carid){
        CarInfo carInfo =  carInfoRepository.findOne(carid);
        if(carInfo != null){
            return new CarInfoByCarIdResponseRoot(HttpStatus.OK.value(), "Success", true,
                    new CarInfoByCarIdResponse(carInfo.getCarInfoId(), carInfo.getCarNumber(), carInfo.getCarModels(), carInfo.getCarType(),
                            carInfo.getOwner(), carInfo.getOwnerContact(), carInfo.getAc(), carInfo.getImage(), carInfo.getStartDate(),
                            carInfo.getDetailsSpecification(), carInfo.getDriverName(), carInfo.getDriverMobile(), carInfo.getDriverRegNo(),
                            carInfo.isActive(), carInfo.getNumberofSeat()));
        }else{
            return new CarInfoByCarIdResponseRoot(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed", false, null);
        }
    }

    /**
     * this function will update car information
     * @param carInfo
     * @return CarInfoUpdateResponse
     */
    @PutMapping(value = "/car/update")
    public CarInfoUpdateResponse carInfoUpdateByCarId(@RequestBody CarInfo carInfo){
        try {
            if(!StringUtils.isEmpty(carInfo.getImage())){
                File file = File.createTempFile("CAR_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+"_",".jpg", CommonTask.getImageDirectory(servletContext));
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(Base64.getDecoder().decode(carInfo.getImage()));
                carInfo.setImage("http://"+CommonTask.getIP()+":"+CommonConstant.PORT+"/cholo-ek-sathe/"+CommonConstant.BASE_IMAGE_PATH+file.getName());
                fos.close();
            }
            if(carInfoRepository.save(carInfo).getCarInfoId()>0){
                return new CarInfoUpdateResponse(HttpStatus.OK.value(), "Car info update successfully done.", true);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new CarInfoUpdateResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Car info update failed.", false);
    }
}
