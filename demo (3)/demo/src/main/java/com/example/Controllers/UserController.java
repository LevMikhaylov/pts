package com.example.Controllers;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.orders.Entities.Validation.User;
import com.example.orders.Services.UserService;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService us;
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user) {
        // Логика регистрации пользователя
        us.addUser(user);
        return "Пользователь зарегистрирован";
    }
    @SuppressWarnings("rawtypes")
    @GetMapping("/export/users")
    public ResponseEntity<byte[]> exportUsersToExcel() {
        try {
            List<User>users  = us.getAllUsers();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = (Sheet) workbook.createSheet("Список пользователей");

            // Создаем формат даты
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            // Создаем заголовок
            Row header = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(0);
            String[] columns = {"ID", "ФИО", "Email", "Телефон", "Дата регистрации"};

            // Стиль для заголовков
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                // Установка стиля жирного шрифта и рамок для заголовков
                cell.getCellStyle().setBorderTop(BorderStyle.THIN);
                cell.getCellStyle().setBorderBottom(BorderStyle.THIN);
                cell.getCellStyle().setBorderLeft(BorderStyle.THIN);
                cell.getCellStyle().setBorderRight(BorderStyle.THIN);
            }

            // Заполняем данными пользователей
            int rowIndex = 1;
            for (User user : users) {
                Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(rowIndex++);
                row.createCell(0).setCellValue(user.getId()); // Предполагается, что у User есть метод getId()
                row.createCell(1).setCellValue(user.getSurname() + " " + user.getName() + " "+ user.getPatr()); // ФИО
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getPhoneNumber());
                row.createCell(4).setCellValue(dateFormat.format(user.getRegistrationDate()));
            }

            // Запись в выходной поток
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=users.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
