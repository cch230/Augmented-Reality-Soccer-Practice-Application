package org.tensorflow.lite.examples.posenet;

public class Bezier  {

    private PathPoint p1,p2,p3,p4;		// 각각 의 조절 포인트 (4차원 까지만 지정)

    private PathPoint[] arrPn;			// n차원의 조절 포인트 저장

    private double mu;					/* 곡선의 시작과 끝을 알리는 변수 ( 0 부터 1사이  )

										0 : 시작, 1: 끝*/



    private PathPoint resultPoint;		// 계산된 결과를 넘겨주는 포인트

    public Bezier() {

        super();



        resultPoint = new PathPoint();

        initResultPoint();

    }



    // 3차원 베지어 곡선 포인터 설정

    public void setBezier3(PathPoint p1 , PathPoint p2 , PathPoint p3){

        this.p1 = p1;

        this.p2 = p2;

        this.p3 = p3;

    }



    // 3차원 베지어 곡선 계산

    public void nextBezier3(){

        double mum1,mum12,mu2;



        initResultPoint();

        mu2 = mu * mu;

        mum1 = 1 - mu;

        mum12 = mum1 * mum1;

        resultPoint.x = p1.x * mum12 + 2 * p2.x * mum1 * mu + p3.x * mu2;

        resultPoint.y = p1.y * mum12 + 2 * p2.y * mum1 * mu + p3.y * mu2;

        resultPoint.z = p1.z * mum12 + 2 * p2.z * mum1 * mu + p3.z * mu2;

    }



    // 4차원 베지어 곡선 포인터 설정

    public void setBezier4(PathPoint p1 , PathPoint p2 , PathPoint p3 , PathPoint p4){

        this.p1 = p1;

        this.p2 = p2;

        this.p3 = p3;

        this.p4 = p4;

    }



    // 4차원 베지어 곡선 계산

    public void nextBezier4(){

        double mum1,mum13,mu3;



        initResultPoint();

        mum1 = 1 - mu;

        mum13 = mum1 * mum1 * mum1;

        mu3 = mu * mu * mu;

        resultPoint.x = mum13*p1.x + 3*mu*mum1*mum1*p2.x + 3*mu*mu*mum1*p3.x + mu3*p4.x;

        resultPoint.y = mum13*p1.y + 3*mu*mum1*mum1*p2.y + 3*mu*mu*mum1*p3.y + mu3*p4.y;

        resultPoint.z = mum13*p1.z + 3*mu*mum1*mum1*p2.z + 3*mu*mu*mum1*p3.z + mu3*p4.z;

    }



    // N차원 베지어 곡선 포인터 설정

    public void setBezierN(PathPoint[] arrPn){

        this.arrPn = arrPn;

    }



    // N차원 베지어 곡선 계산

    public void nextBezierN(){

        int k,kn,nn,nkn;

        double blend,muk,munk;

        int n = arrPn.length-1;



        initResultPoint();

        muk = 1;

        munk = Math.pow(1-mu,(double)n);

        for (k=0;k<=n;k++)

        {

            nn = n;

            kn = k;

            nkn = n - k;

            blend = muk * munk;

            muk *= mu;

            munk /= (1-mu);

            while (nn >= 1) {

                blend *= nn;

                nn--;

                if (kn > 1) {

                    blend /= (double)kn;

                    kn--;

                }

                if (nkn > 1) {

                    blend /= (double)nkn;

                    nkn--;

                }

            }

            resultPoint.x += arrPn[k].x * blend;

            resultPoint.y += arrPn[k].y * blend;

            resultPoint.z += arrPn[k].z * blend;

        }

    }


    // 결과값을 초기화


    public void initResultPoint(){

        resultPoint.x = 0.0;

        resultPoint.y = 0.0;

        resultPoint.z = 0.0;

    }



    // u변수를 설정

    public void setMu(double mu){

        this.mu = mu;

    }

    // 계산 결과값을 넘긴다.
    public PathPoint getResult(){

        return resultPoint;

    }

}