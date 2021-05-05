import axios from 'axios';

const API_URL = '/api/topics';

/**
 * Fetch topics
 * @returns {Promise} Topics
 */
export function getTopics() {
    return axios.get(`${API_URL}`);
}

/**
 * Get terms for a topic
 * @param {number} topicId
 * @returns {Promise} Term
 */
export function getTermsForTopic(topicId) {
    return axios.get(`${API_URL}/${topicId}/terms`);
}

/**
 * Create term for a topic
 * @param {number} topicId
 * @param {string} term
 * @returns {Promise} Created term
 */
export function createTerm(topicId, term) {
    return axios.post(`${API_URL}/${topicId}/terms`, term);
}
