package deliveryservice.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

// 날짜를 입력받기 위해 띄우는 팝업 다이얼로그(달력) 클래스입니다.
// 직접 입력하는 방식보다 달력을 보여주는 것이 사용자에게 더 편리할 것 같아 JDialog를 상속받아 구현했습니다.
public class DatePicker extends JDialog {
    // 현재 월과 년도를 저장합니다. 초기값은 현재 시스템 시간입니다.
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel l = new JLabel("", JLabel.CENTER); // 상단에 "yyyy년 MM월"을 표시하는 레이블
    String day = ""; // 사용자가 선택한 날짜(일)를 저장하는 변수
    JDialog d; // 실제 팝업 창
    JButton[] button = new JButton[49]; // 요일 7개 + 날짜 최대 42개 = 49개의 버튼 배열

    public DatePicker(JFrame parent) {
        d = new JDialog();
        // setModal(true)로 설정하여 이 팝업이 떠있는 동안은 다른 창을 클릭할 수 없도록 막았습니다. (필수 설정)
        d.setModal(true);
        String[] header = { "일", "월", "화", "수", "목", "금", "토" }; // 요일 헤더

        // 날짜를 표시할 그리드 패널: 7x7 배열입니다.
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(430, 120));

        // 49개의 버튼을 생성하고 이벤트 리스너를 붙입니다.
        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.white);

            // 7번 버튼(두 번째 줄 첫 번째 칸)부터는 날짜 버튼이므로 클릭 이벤트를 추가합니다.
            if (x > 6) {
                button[x].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        day = button[selection].getActionCommand(); // 버튼의 텍스트(일자)를 가져옴
                        d.dispose(); // 선택 후 창 닫기
                    }
                });
            }
            // 0번부터 6번까지는 요일 헤더를 표시합니다.
            if (x < 7) {
                button[x].setText(header[x]);
                button[x].setForeground(Color.red); // 일요일은 빨간색으로 표시
            }
            p1.add(button[x]);
        }

        // 월 이동 버튼 패널
        JPanel p2 = new JPanel(new GridLayout(1, 3));

        // '이전 달' 버튼
        JButton previous = new JButton("<< 이전 달");
        previous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month--; // 월을 1 감소시키고
                displayDate(); // 달력을 다시 그립니다.
            }
        });

        // '다음 달' 버튼
        JButton next = new JButton("다음 달 >>");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                month++; // 월을 1 증가시키고
                displayDate(); // 달력을 다시 그립니다.
            }
        });

        p2.add(previous);
        p2.add(l); // 현재 년/월 레이블
        p2.add(next);

        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack(); // 컴포넌트 크기에 맞춰 창 크기를 조정합니다.
        d.setLocationRelativeTo(parent); // 부모 창의 중앙에 팝업이 뜨도록 설정했습니다.
        displayDate(); // 날짜 계산 및 초기 표시
        d.setVisible(true); // 창을 보여줍니다.
    }

    // 달력의 날짜를 계산하고 버튼에 표시하는 핵심 로직입니다.
    public void displayDate() {
        // 이전 달에 표시되었던 날짜 텍스트를 모두 지웁니다.
        for (int x = 7; x < button.length; x++)
            button[x].setText("");

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy년 MM월");
        java.util.Calendar cal = java.util.Calendar.getInstance();

        // 선택된 년/월의 1일로 캘린더를 설정합니다.
        cal.set(year, month, 1);

        // 1일이 무슨 요일인지 계산합니다. (일요일=1, 월요일=2, ...)
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);

        // 해당 월이 며칠까지 있는지(최대 일수)를 계산합니다. (30일, 31일, 윤년 등 자동 처리)
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        // 달력 버튼 배열에 날짜를 채워 넣습니다.
        // x는 버튼 배열의 인덱스입니다. 요일 헤더 6칸 + 1일의 요일 위치부터 시작합니다.
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
            button[x].setText("" + day);

        // 상단 레이블에 현재 년월을 표시합니다.
        l.setText(sdf.format(cal.getTime()));
        d.setTitle("날짜 선택");
    }

    // 선택된 날짜를 외부(OrderInsertView)로 반환하는 메소드입니다.
    public String setPickedDate() {
        if (day.equals("")) return ""; // 날짜 선택 없이 창을 닫았을 경우 빈 문자열 반환

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();

        // 현재 선택된 년, 월, 일로 캘린더 객체를 다시 설정합니다.
        cal.set(year, month, Integer.parseInt(day));

        // DB나 텍스트 필드에 사용하기 편한 "YYYY-MM-DD" 포맷으로 변환하여 반환합니다.
        return sdf.format(cal.getTime());
    }
}