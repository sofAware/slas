package kr.sofaware.slas.function.mainpage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
public class CellDto {
    public static int TOTAL_PERIOD=7;                       // 교시는 7교시까지
    public static int TOTAL_DAYOFWEEK=5;                    // 요일은 월화수목금 이렇게 5개만

    private String lectureName = null;
    private int cellSpan = 1;

    @Builder
    public CellDto(String lectureName, int cellSpan) {
        this.lectureName = lectureName;
        this.cellSpan = cellSpan;
    }

    public static List<ArrayList<CellDto>> createCellDtoList(List<SyllabusDto> syllabusDtoList) {          // 시간표 출력 위한 cellDtoList 생성
        List<ArrayList<CellDto>> cellDtoList = new ArrayList<>(TOTAL_PERIOD);
        for (int i = 0; i < TOTAL_PERIOD; i++)
            cellDtoList.add(new ArrayList<CellDto>(TOTAL_DAYOFWEEK));

        if (!syllabusDtoList.isEmpty()) {
            for (int i = 0; i < TOTAL_PERIOD; i++) {
                for (int j = 0; j < TOTAL_DAYOFWEEK; j++) {
                    cellDtoList.get(i).add(new CellDto());
                }
            }

            for (SyllabusDto s : syllabusDtoList) {
                switch (s.getDayOfWeek1()) {
                    case "MON":
                        cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(0).setLectureName(s.getName());
                        break;
                    case "TUE":
                        cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(1).setLectureName(s.getName());
                        break;
                    case "WED":
                        cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(2).setLectureName(s.getName());
                        break;
                    case "THU":
                        cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(3).setLectureName(s.getName());
                        break;
                    case "FRI":
                        cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(4).setLectureName(s.getName());
                        break;
                }
                switch (s.getDayOfWeek2()) {
                    case "MON":
                        cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(0).setLectureName(s.getName());
                        break;
                    case "TUE":
                        cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(1).setLectureName(s.getName());
                        break;
                    case "WED":
                        cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(2).setLectureName(s.getName());
                        break;
                    case "THU":
                        cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(3).setLectureName(s.getName());
                        break;
                    case "FRI":
                        cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(4).setLectureName(s.getName());
                        break;
                }
            }

            for (int i = 0; i < TOTAL_PERIOD - 1; i++) {                             // span 처리
                int j = 0;
                for (CellDto currentCell : cellDtoList.get(i)) {
                    CellDto underCell = cellDtoList.get(i + 1).get(j);
                    if ((currentCell.getLectureName() != null) && (underCell.getLectureName() != null) && (currentCell.getLectureName().equals(underCell.getLectureName()))) {
                        currentCell.setCellSpan(2);
                        cellDtoList.get(i + 1).remove(j);
                    }
                    j++;
                }
            }
        }

        return cellDtoList;
    }
}