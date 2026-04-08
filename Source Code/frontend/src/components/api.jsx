import axios from 'axios';
import { toast } from 'sonner';

const api = axios.create({
    baseURL: 'http://localhost:8080',
});

let isSessionExpiredHandled = false; 


api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => Promise.reject(error)
);


api.interceptors.response.use(
    (response) => response,
    (error) => {

        if (error.response && error.response.status === 401) {

            if (!isSessionExpiredHandled) {
                isSessionExpiredHandled = true;

                
                toast.error("Session expired. Please login again.");

                
                localStorage.clear();

                
                setTimeout(() => {
                    if (!window.location.pathname.includes('/login')) {
                        window.location.href = '/login';
                    }
                }, 1500);
            }
        }

        return Promise.reject(error);
    }
);

export default api;