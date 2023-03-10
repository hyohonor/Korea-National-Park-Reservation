<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
/* section calendar */

.sec_cal {
    width: 360px;
    margin: 0 auto;
    font-family: "NotoSansR";
}

.sec_cal .cal_nav {
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 700;
    font-size: 48px;
    line-height: 78px;
}

.sec_cal .cal_nav .year-month {
    width: 300px;
    text-align: center;
    line-height: 1;
}

.sec_cal .cal_nav .nav {
    display: flex;
    border: 1px solid #333333;
    border-radius: 5px;
}

.sec_cal .cal_nav .go-prev,
.sec_cal .cal_nav .go-next {
    display: block;
    width: 50px;
    height: 78px;
    font-size: 0;
    display: flex;
    justify-content: center;
    align-items: center;
}

.sec_cal .cal_nav .go-prev::before,
.sec_cal .cal_nav .go-next::before {
    content: "";
    display: block;
    width: 20px;
    height: 20px;
    border: 3px solid #000;
    border-width: 3px 3px 0 0;
    transition: border 0.1s;
}

.sec_cal .cal_nav .go-prev:hover::before,
.sec_cal .cal_nav .go-next:hover::before {
    border-color: #ed2a61;
}

.sec_cal .cal_nav .go-prev::before {
    transform: rotate(-135deg);
}

.sec_cal .cal_nav .go-next::before {
    transform: rotate(45deg);
}

.sec_cal .cal_wrap {
    padding-top: 40px;
    position: relative;
    margin: 0 auto;
}

.sec_cal .cal_wrap .days {
    display: flex;
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ddd;
}

.sec_cal .cal_wrap::after {
    top: 368px;
}

.sec_cal .cal_wrap .day {
    display:flex;
    align-items: center;
    justify-content: center;
    width: calc(100% / 7);
    text-align: left;
    color: #999;
    font-size: 12px;
    text-align: center;
    border-radius:5px
}

.current.today {background: rgb(242 242 242);}

.sec_cal .cal_wrap .dates {
    display: flex;
    flex-flow: wrap;
    height: 290px;
}

.sec_cal .cal_wrap .day:nth-child(7n -1) {
    color: #3c6ffa;
}

.sec_cal .cal_wrap .day:nth-child(7n) {
    color: #ed2a61;
}

.sec_cal .cal_wrap .day.disable {
    color: #ddd;
}

</style>

<script type="text/javascript">

$(document).ready(function() {
    calendarInit();
});
/*
    ?????? ????????? ??? ??? ????????? ?????? ?????? 

    ?????? ???(????????? : ?????? ??????)
    ?????? ???????????? ????????? ??????
    ?????? ???????????? ????????? ??????
*/

function calendarInit() {

    // ?????? ?????? ????????????
    var date = new Date(); // ?????? ??????(?????? ??????) ????????????
    var utc = date.getTime() + (date.getTimezoneOffset() * 60 * 1000); // uct ????????? ??????
    var kstGap = 9 * 60 * 60 * 1000; // ?????? kst ???????????? ?????????
    var today = new Date(utc + kstGap); // ?????? ???????????? date ?????? ?????????(??????)
  
    var thisMonth = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    // ???????????? ???????????? ?????? ??????
  
    
    var currentYear = thisMonth.getFullYear(); // ???????????? ???????????? ???
    var currentMonth = thisMonth.getMonth(); // ???????????? ???????????? ???
    var currentDate = thisMonth.getDate(); // ???????????? ???????????? ???

    // kst ?????? ????????????
    // console.log(thisMonth);

    // ????????? ?????????
    renderCalender(thisMonth);

    function renderCalender(thisMonth) {

        // ???????????? ?????? ????????? ??????
        currentYear = thisMonth.getFullYear();
        currentMonth = thisMonth.getMonth();
        currentDate = thisMonth.getDate();

        // ?????? ?????? ????????? ??? ????????? ?????? ?????????
        var startDay = new Date(currentYear, currentMonth, 0);
        var prevDate = startDay.getDate();
        var prevDay = startDay.getDay();

        // ?????? ?????? ???????????? ????????? ?????? ?????????
        var endDay = new Date(currentYear, currentMonth + 1, 0);
        var nextDate = endDay.getDate();
        var nextDay = endDay.getDay();

        // console.log(prevDate, prevDay, nextDate, nextDay);

        // ?????? ??? ??????
        $('.year-month').text(currentYear + '.' + (currentMonth + 1));

        // ????????? html ?????? ??????
        calendar = document.querySelector('.dates')
        calendar.innerHTML = '';
        
        // ?????????
        for (var i = prevDate - prevDay + 1; i <= prevDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day prev disable">' + i + '</div>'
        }
        // ?????????
        for (var i = 1; i <= nextDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day current">' + i + '</div>'
        }
        // ?????????
        for (var i = 1; i <= (7 - nextDay == 7 ? 0 : 7 - nextDay); i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day next disable">' + i + '</div>'
        }

        // ?????? ?????? ??????
        if (today.getMonth() == currentMonth) {
            todayDate = today.getDate();
            var currentMonthDate = document.querySelectorAll('.dates .current');
            currentMonthDate[todayDate -1].classList.add('today');
        }
    }

    // ???????????? ??????
    $('.go-prev').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth - 1, 1);
        renderCalender(thisMonth);
    });

    // ???????????? ??????
    $('.go-next').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth + 1, 1);
        renderCalender(thisMonth); 
    });
}
</script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	/* section calendar */
.sec_cal {
    width: 360px;
    margin: 0 auto;
    font-family: "NotoSansR";
}

.sec_cal .cal_nav {
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 700;
    font-size: 48px;
    line-height: 78px;
}

.sec_cal .cal_nav .year-month {
    width: 300px;
    text-align: center;
    line-height: 1;
}

.sec_cal .cal_nav .nav {
    display: flex;
    border: 1px solid #333333;
    border-radius: 5px;
}

.sec_cal .cal_nav .go-prev,
.sec_cal .cal_nav .go-next {
    display: block;
    width: 50px;
    height: 78px;
    font-size: 0;
    display: flex;
    justify-content: center;
    align-items: center;
}

.sec_cal .cal_nav .go-prev::before,
.sec_cal .cal_nav .go-next::before {
    content: "";
    display: block;
    width: 20px;
    height: 20px;
    border: 3px solid #000;
    border-width: 3px 3px 0 0;
    transition: border 0.1s;
}

.sec_cal .cal_nav .go-prev:hover::before,
.sec_cal .cal_nav .go-next:hover::before {
    border-color: #ed2a61;
}

.sec_cal .cal_nav .go-prev::before {
    transform: rotate(-135deg);
}

.sec_cal .cal_nav .go-next::before {
    transform: rotate(45deg);
}

.sec_cal .cal_wrap {
    padding-top: 40px;
    position: relative;
    margin: 0 auto;
}

.sec_cal .cal_wrap .days {
    display: flex;
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ddd;
}

.sec_cal .cal_wrap::after {
    top: 368px;
}

.sec_cal .cal_wrap .day {
    display:flex;
    align-items: center;
    justify-content: center;
    width: calc(100% / 7);
    text-align: left;
    color: #999;
    font-size: 12px;
    text-align: center;
    border-radius:5px
}

.current.today {background: rgb(242 242 242);}

.sec_cal .cal_wrap .dates {
    display: flex;
    flex-flow: wrap;
    height: 290px;
}

.sec_cal .cal_wrap .day:nth-child(7n -1) {
    color: #3c6ffa;
}

.sec_cal .cal_wrap .day:nth-child(7n) {
    color: #ed2a61;
}

.sec_cal .cal_wrap .day.disable {
    color: #ddd;
}
</style>
<script>
	$(document).ready(function() {
	    calendarInit();
	});

	function calendarInit() {

	    // ?????? ?????? ????????????
	    var date = new Date(); // ?????? ??????(?????? ??????) ????????????
	    var utc = date.getTime() + (date.getTimezoneOffset() * 60 * 1000); // uct ????????? ??????
	    var kstGap = 9 * 60 * 60 * 1000; // ?????? kst ???????????? ?????????
	    var today = new Date(utc + kstGap); // ?????? ???????????? date ?????? ?????????(??????)
	  
	    var thisMonth = new Date(today.getFullYear(), today.getMonth(), today.getDate());
	    // ???????????? ???????????? ?????? ??????
	  
	    
	    var currentYear = thisMonth.getFullYear(); // ???????????? ???????????? ???
	    var currentMonth = thisMonth.getMonth(); // ???????????? ???????????? ???
	    var currentDate = thisMonth.getDate(); // ???????????? ???????????? ???

	    // kst ?????? ????????????
	    // console.log(thisMonth);

	    // ????????? ?????????
	    renderCalender(thisMonth);

	    function renderCalender(thisMonth) {

	        // ???????????? ?????? ????????? ??????
	        currentYear = thisMonth.getFullYear();
	        currentMonth = thisMonth.getMonth();
	        currentDate = thisMonth.getDate();

	        // ?????? ?????? ????????? ??? ????????? ?????? ?????????
	        var startDay = new Date(currentYear, currentMonth, 0);
	        var prevDate = startDay.getDate();
	        var prevDay = startDay.getDay();

	        // ?????? ?????? ???????????? ????????? ?????? ?????????
	        var endDay = new Date(currentYear, currentMonth + 1, 0);
	        var nextDate = endDay.getDate();
	        var nextDay = endDay.getDay();

	        // console.log(prevDate, prevDay, nextDate, nextDay);

	        // ?????? ??? ??????
	        $('.year-month').text(currentYear + '.' + (currentMonth + 1));

	        // ????????? html ?????? ??????
	        calendar = document.querySelector('.dates')
	        calendar.innerHTML = '';
	        
	        // ?????????
	        for (var i = prevDate - prevDay + 1; i <= prevDate; i++) {
	            calendar.innerHTML = calendar.innerHTML + '<div class="day prev disable">' + i + '</div>'
	        }
	        // ?????????
	        for (var i = 1; i <= nextDate; i++) {
	            calendar.innerHTML = calendar.innerHTML + '<div class="day current">' + i + '</div>'
	        }
	        // ?????????
	        for (var i = 1; i <= (7 - nextDay == 7 ? 0 : 7 - nextDay); i++) {
	            calendar.innerHTML = calendar.innerHTML + '<div class="day next disable">' + i + '</div>'
	        }

	        // ?????? ?????? ??????
	        if (today.getMonth() == currentMonth) {
	            todayDate = today.getDate();
	            var currentMonthDate = document.querySelectorAll('.dates .current');
	            currentMonthDate[todayDate -1].classList.add('today');
	        }
	    }

	    // ???????????? ??????
	    $('.go-prev').on('click', function() {
	        thisMonth = new Date(currentYear, currentMonth - 1, 1);
	        renderCalender(thisMonth);
	    });

	    // ???????????? ??????
	    $('.go-next').on('click', function() {
	        thisMonth = new Date(currentYear, currentMonth + 1, 1);
	        renderCalender(thisMonth); 
	    });
	}
</script>
</head>
<body>

<<<<<<< HEAD
<div class="sec_cal">
  <div class="cal_nav">
    <a href="javascript:;" class="nav-btn go-prev">prev</a>
    <div class="year-month"></div>
    <a href="javascript:;" class="nav-btn go-next">next</a>
  </div>
  <div class="cal_wrap">
    <div class="days">
      <div class="day">MON</div>
      <div class="day">TUE</div>
      <div class="day">WED</div>
      <div class="day">THU</div>
      <div class="day">FRI</div>
      <div class="day">SAT</div>
      <div class="day">SUN</div>
    </div>
    <div class="dates"></div>
  </div>
</div>

hello world!!
hello

</body>
</html>