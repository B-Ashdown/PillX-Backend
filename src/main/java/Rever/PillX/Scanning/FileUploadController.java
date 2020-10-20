package Rever.PillX.Scanning;

import Rever.PillX.Medicine.Medicine;
import Rever.PillX.Medicine.MedicineRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@RestController
public class FileUploadController {

    @Autowired
    MedicineRepository medicineRepository;

    @RequestMapping(value = "/scanning", method = RequestMethod.POST)
    public List<Map<String, String>> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        System.err.println("GOT REQUEST\n\n\n\n\n\n\n\n");
        File convFile= convert(file);
        try {
            if (convFile == null) {
                return null;
            }
            Tesseract tesseract = new Tesseract();
            tesseract.setOcrEngineMode(0); // 0 - Tesseract, 1 - Cube, 2 - Tesseract & Cube
            tesseract.setDatapath("/home/PillX-Backend/tessdata/");
            String text = tesseract.doOCR(convFile);
            if (!convFile.delete()) {
                System.out.println("Failed delete");
            }
            System.out.println("FOUND TEXT " + text + " \n\n\n\n\n");
            List<Medicine> medicineList = medicineRepository.findAll();
            List<Map<String, String>> results = new ArrayList<>();
            for (Medicine medicine : medicineList) {
                if (medicine.identifier.contains(text) || medicine.name.contains(text)) {
                    Map<String, String> medicineInfo = new TreeMap<>();
                    medicineInfo.put("identifier", medicine.identifier);
                    medicineInfo.put("name", medicine.name);
                    results.add(medicineInfo);
                }
            }
            if (results.size() == 0) {
                Medicine defaultMedicine = medicineRepository.findByidentifier("97801");
                Map<String, String> defaulMedicineInfo = new TreeMap<>();
                defaulMedicineInfo.put("identifier", defaultMedicine.identifier);
                defaulMedicineInfo.put("name", defaultMedicine.name);
                results.add(defaulMedicineInfo);
            }
            return results;
        } catch (Exception ex) {
            if (!convFile.delete()) {
                System.out.println("Failed delete after catching Exception");
            }
            throw ex;
        }
    }

    public static File convert(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            System.out.println("No original Filename");
            return null;
        }
        File convFile = new File(file.getOriginalFilename());
        if (!convFile.createNewFile()) {
            if (!convFile.delete()) {
                System.out.println("Failed delete");
                return null;
            }
            if (!convFile.createNewFile()) {
                System.out.println("Failed to create file");
                return null;
            }
        }
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}