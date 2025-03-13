import http from "k6/http";
import { sleep, check } from "k6";
import { Trend, Rate } from "k6/metrics";

// metrics
let responseTime = new Trend("response_time");
let successRate = new Rate("success_rate");

export let options = {
	stages: [
		{ duration: "1m", target: 100 }, // ramp-up to 100 users
		{ duration: "2m", target: 300 }, // ramp-up to 300 users
		{ duration: "2m", target: 500 }, // ramp-up to 500 users
		{ duration: "3m", target: 500 }, // sustain 500 users
		{ duration: "1m", target: 0 }, // ramp-down to 0 users
	],
	thresholds: {
		http_req_duration: ["p(95)<500"], // 95% of requests should be below 500ms
		success_rate: ["rate>0.95"], // Success rate should be above 95%
	},
};

export default function () {
	let res = http.get("http://csc8113-group8.csproject.org/api/catalog/books");

	// record
	responseTime.add(res.timings.duration);
	successRate.add(res.status === 200);

	// HTTP response
	check(res, {
		"status is 200": (r) => r.status === 200,
		"response time": (r) => r.timings.duration < 500,
	});

	sleep(1);
}
