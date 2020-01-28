# 증강현실 축구 연습 어플리케이션
## Augmented-Reality-Soccer-Practice-Application
사용자가 코치의 도움없이 여러 훈련을 개인적으로 할 수 있도록 도와주는 어플입니다.

It is a soccer-practice-application that helps the user to do various training personally without the help of a coach.

### 이찬솔 이기열 이교범 최철환
## Representative email : cch01024857239@gmail.com

### 이 문서와 프로젝트는 한국산업기술대학교 컴퓨터공학부의 “종합설계”교과목에서 프로젝트“스마트 축구 연습 어플리케이션”을 수행하는  팀원(팀번호: S5-2)들이 작성한 것으로 사용하기 위해서는 팀원들의 허락이 필요합니다.
### These document and project are written by team members (Team No .: S5-2) who perform the project “Augmented-Reality-Soccer-Practice-Application” in the “Graduation Thesis” course of the Korea Polytechnic University.
## Copyright 2020. "Graduation Thesis Team No .S5-2 of the Korea Polytechnic University" All Pictures Cannot Be Copied Wihour Permission.

# 개발환경 (Development environment)
환경|버전
---|---|
Android Studio|3.4.2|
Aws RDS-MY SQL|8.0.15|
Ubuntu Server|18.0.4|
PyCharm|2019.3|

# 목차
![Contents](./image/start.JPG)

# 서론
## 1.1 작품선정 배경 및 필요성
20세기부터 축구는 이미 세계 인기 스포츠, 시장규모, 선수 연봉에서 1위라는 순위를 유지하고 있다. 이러한 인기로 인해 축구를 배우고자 하는 사람은 수많이 존재하고 있다. 하지만 축구를 배우기 위한 레슨에는 시간당 36,000원, 유학에는 2개월에 약 360만 원이라는 고비용이 필요하다. 보다 적은 비용으로 혼자 다양한 연습을 할 수 있는 대안이 필요하고, 코치 없이 실시간으로 피드백을 받을 수 있는 환경이 필요하다고 생각하여 졸업작품으로 선정하였다. 

## 1.2 기존 연구/기술 동향 분석
관련 제품으로는 스마트폰을 이용한 농구 연습 어플리케이션인 홈코트가 존재한다. 이 제품은 증강현실을 이용하여 혼자서 슛, 드리블의 훈련이 가능한 농구 연습 어플리케이션으로 인공지능을 이용한 자세 교정을 통해 비싼 코치 없이 혼자 연습이 가능하다는 장점을 갖고 있다. 하지만 연습 가능한 스포츠가 농구로 한정되어 있다.

## 1.3 개발 목표
 최종 목표로는 스마트 축구 연습 어플리케이션을 개발하여, 보다 적은 비용으로 다양한 연습을 도와줌과 동시에 개인 단위로 혼자 연습할 수 있는 환경을 만들어 주는 것이다. 단계별로 들어가자면 일단 opencv를 이용하여 파이참과 안드로이드 스튜디오 환경에서 공을 인식하여 공의 궤적과 속력, 도착한 지점 등을 영상처리를 통한 시각화 구현과 간단한 UI 작성과 서버 구축이다. 다음 단계로는 opencv를 이용한 자세 인식의 신경망 및 딥러닝 구축으로 파이참 환경에서 신경망의 기본 형식을 구현한 후 Tensarflow를 이용하여 세부적인 부분을 추가하여 신경망을 구축한다. 그 후 사용자의 자세를 신경망에 학습된 자세와 비교하여 사용자의 자세를 교정하는 것을 구축한다. 마지막 단계로는 1단계에 간단히 작성되었던 UI를 좀 더 사용자 편의에 맞게 구축하고 어플리케이션으로 쉽게 연습 선택, 영상 시청, 연습 기록을 수치화 등의 기능을 제공하고 서버에 저장된 영상을 사용자에게 제공할 수 있게 인터페이스를 구축한다. 

## 1.4 팀 역할 분담
 역할 분담으로는 인공지능, 영상처리, 앱 UI 작성, DB 및 서버 구축으로 4개로 나누어 각자 2개씩 맡음으로써 혼자서 해결하기 어려운 문제나 막히는 부분을 상호보완하여 해결할 수 있도록 하였다. 각자 역할로는 이찬솔 인공지능-영상처리. 이기열 영상처리-DB 및 서버 구축, 이교범 인공지능, 영상처리. 최철환 앱 UI 작성, DB 및 서버 구축을 맡도록 하였다. 산출물 관리는 개인 PC와 구글드라이브, 개인 메일로 3개에 공간에 백업, 보관하며 코드작성은 Github를 활용하여 수정 부분을 알 수 있도록 하였다.

## 1.5 개발 일정 
 개발 일정은 다음의 표대로 진행할 수 있도록 일정을 잡았고 매주 금요일마다 만나 서로 작성한 것을 공유하고 진행 상황을 알 수 있도록 하였다.
![calendar](./image/m1.JPG)

## 1.6 개발 환경
 개발 환경으로는 앱을 작성할 수 있도록 안드로이드 스튜디오를 사용하고 언어로는 자바를 사용하여 작성한다. 사용하는 스튜디오 버전은 3.4.2이다. 안드로이드 타깃은 최소 8.1(오레오)_API Level 27 으로, 최대 타깃은 10.0(안드로이드 10)_API Level 29이다. 다음으로 DB는 아마존에서 제공하는 AWS-RDS를 사용하여 MY-sql DB를 구축하고 서버와 연동할 수 있도록 하였다. 사용 언어로는 sql문을, 버전은 8.0.15를 사용한다. 서버 역시 마찬가지로 아마존에서 제공하는 AWS-EC2를 사용해 unbuntu 환경을 구축하고 사용 언어로 c++을 사용하여 버전 18.0.4에 구축한다. 마지막으로 영상처리, 인공지능 부분은 파이참 환경에서 파이썬을 가지고 작성할 것이다. 버전은 2019.3버전을 사용한다.
 ![environment](./image/m2.JPG)

# 본론
## 2.1 개발 내용
### UI
 ![UI](./image/ui1.JPG)
 ![UI](./image/ui2.JPG)
 ![UI](./image/ui3.JPG)
 ![UI](./image/ui4.JPG)
 ![UI](./image/ui5.JPG)
 ![UI](./image/ui6.JPG)
 ![UI](./image/ui7.JPG)

### 영상 처리
  ![Image processing](./image/v1.JPG)

### 서버 구축
  ![Server](./image/s1.JPG)
  ![Server](./image/s2.JPG)
 
## 2.2 문제 및 해결방안
* Ubuntu 서버와 Mysql DB간에 보안으로 인한 접속 문제가 발생하였다. 이를 해결한 방법으로는 Mysql DB에 접속할 특정 IP를 인바운드 규칙에서 허용하여 해당 IP주소에서 DB에 접속할 수 있도록 함으로써 해결해야 했다. 
* 자바와 Python 연동 문제로는 Kivy를 이용하여 안드로이드 위에 파이썬 코드를 올리는 방법을 이용하여 해결하였다.
* 공 트래킹을 할 때에 색을 따라 트래킹을 하도록 하였는데 이 방식이 아닌 원이나 아니면 축구공을 인식할 다른 방법을 찾아서 트래킹을 하는 방법이 필요하였다. 원 검출을 이용하기 위해서 cv.HounghCircles()함수를 이용하여 해결하는 방법을 시도해 보고 있으나 아직 해결하지 못하였다.
## 2.3 시험 시나리오 
총 3가지의 시나리오를 진행하였다. 처음으로 공 감지로 카메라에서 초록색 물체를 감지하게 되면 초록색 물체를 공으로 인식한 후 그 테두리에 원을 그려준다. 그 후 이동 경로를 따라 선을 그어준다.
 다음으로는 서버 통신으로 aws-ec2를 이용하여 ubuntu 서비스를 구축한 후 서버에서 my-sql을 이용하여 db를 수집한다. 그 후 서버에서 aws-rds를 연동하여 db에 저장한다. 마지막으로 애플리케이션 통신으로 사용자가 애플리케이션을 통해 요구사항을 입력하게 되면 입력된 데이터를 db에 저장한다.
# 참고 문헌
*  [앱과 DB연동](https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/java-rds.html)
*  [외부 클라이언트와 DB서버 연결](https://docs.aws.amazon.com/ko_kr/AmazonRDS/latest/UserGuide/USER_VPC.html)
* [딥러닝 라이브러리 언어별 종류](https://aikorea.org/blog/dl-libraries)
* [딥러닝에 대해](http://t-robotics.blogspot.com/2015/05/deep-learning.html#.XfOaHegzaUk)
* [트래킹 코드](https://diy-project.tistory.com/96)
* [파이썬 객체 감지](https://dlsdn73.tistory.com/1055)
* [공 관련 메소드](https://liveupdate.tistory.com/281)
* [open cv ROI 추출](https://yeolco.tistory.com/57)
*[Keras 라이브러리](https://tykimos.github.io/lecture/)
* [Tensorflow 라이브러리](https://blog.naver.com/beyondlegend/221485865659)
* [Tensorflow 환경 구축](https://brunch.co.kr/@gnugeun/20)
* > Haar-like feature 프로토타입 관련- 2011_송자혜, 장연진_강원대학교 IT대학 전기전자공학부 전기전자공학 졸업논문

 













