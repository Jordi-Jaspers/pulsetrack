declare global {
	namespace App {}
}

// ======================== MODELS ===========================

// ======================== EXCEPTION ========================
class HttpException {
	method: string;
	uri: string;
	query: null;
	contentType: string;
	statusCode: number;
	statusMessage: string;
	errorMessage: string;
}
class ApiException extends HttpException {
	apiErrorCode: string;
	apiErrorReason: string;
}

class ValidationException extends HttpException {
	errors: ValidationError[];
}

class ValidationError {
	code: string;
	field: string;
}

// ======================== REQUESTS ========================

// ======================== RESPONSE ========================

// ======================== JWT ========================
